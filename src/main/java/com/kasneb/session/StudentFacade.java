/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.client.ExamPaper;
import com.kasneb.client.Receipt;
import com.kasneb.client.ReceiptDetail;
import com.kasneb.client.Registration;
import com.kasneb.client.Renewal;
import com.kasneb.entity.Contact;
import com.kasneb.entity.Country;
import com.kasneb.entity.ExamCentre;
import com.kasneb.entity.Exemption;
import com.kasneb.entity.ExemptionPaper;
import com.kasneb.entity.ExemptionStatus;
import com.kasneb.entity.Invoice;
import com.kasneb.entity.KasnebCourse;
import com.kasneb.entity.KasnebStudentQualification;
import com.kasneb.entity.Login;
import com.kasneb.entity.Paper;
import com.kasneb.entity.PaperStatus;
import com.kasneb.entity.Payment;
import com.kasneb.entity.PaymentDetail;
import com.kasneb.entity.Sitting;
import com.kasneb.entity.Student;
import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.StudentCourseSitting;
import com.kasneb.entity.StudentCourseSittingPaper;
import com.kasneb.entity.StudentCourseSittingStatus;
import com.kasneb.entity.StudentCourseStatus;
import com.kasneb.entity.StudentCourseSubscription;
import com.kasneb.entity.User;
import com.kasneb.entity.VerificationStatus;
import com.kasneb.entity.pk.StudentCourseSubscriptionPK;
import com.kasneb.entity.pk.StudentQualificationPK;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.model.LoginResponse;
import com.kasneb.util.CoreUtil;
import com.kasneb.util.DateUtil;
import com.kasneb.util.DigitUtil;
import com.kasneb.util.SecurityUtil;
import com.kasneb.util.WalletUtil;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author jikara
 */
@Stateless
public class StudentFacade extends AbstractFacade<Student> {
    
    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    @EJB
    com.kasneb.session.SittingFacade sittingFacade;
    @EJB
    com.kasneb.session.StudentCourseSittingFacade studentCourseSittingFacade;
    @EJB
    com.kasneb.session.PaperFacade paperFacade;
    @EJB
    com.kasneb.session.LoginFacade loginFacade;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public StudentFacade() {
        super(Student.class);
    }
    
    @Override
    public List<Student> findAll() {
        TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s ORDER BY s.id DESC", Student.class);
        query.setMaxResults(500);
        return query.getResultList();
    }
    
    public List<Student> findAll(Date startDate, Date endDate) {
        TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s WHERE s.created BETWEEN :startDate AND :endDate ORDER BY s.id DESC", Student.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setMaxResults(500);
        return query.getResultList();
    }
    
    public void verifyEmail(String verificationToken) throws CustomHttpException {
        Student student;
        Query query = getEntityManager().createQuery("SELECT s FROM Student s WHERE s.loginId.verificationToken=:verificationToken");
        query.setParameter("verificationToken", verificationToken);
        query.setMaxResults(1);
        try {
            Integer studentId = SecurityUtil.parseJWT(verificationToken);
            student = getEntityManager().find(Student.class, studentId);
            if (student.getLoginId().isEmailActivated()) {
                throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Account already activated");
            }
            student = (Student) query.getSingleResult();
            student.getLoginId().setEmailActivated(true); //Mark as activated
            student.getLoginId().setVerificationToken(null);//Set verification key to null
            getEntityManager().merge(student);
            
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Expired token");
        } catch (NoResultException e) {
            throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Token does not exist");
        } catch (CustomHttpException ex) {
            throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Invalid token");
        }
    }
    
    public void verifyPhoneNumber(String smsToken) throws CustomHttpException {
        Student student;
        Query query = getEntityManager().createQuery("SELECT s FROM Student s WHERE s.loginId.smsToken =:smsToken");
        query.setParameter("smsToken", smsToken);
        query.setMaxResults(1);
        try {
            Integer studentId = SecurityUtil.parseSmsToken(smsToken);
            student = getEntityManager().find(Student.class, studentId);
            if (student.getLoginId().isPhoneNumberActivated()) {
                throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Account already activated");
            }
            student = (Student) query.getSingleResult();
            student.getLoginId().setPhoneNumberActivated(true); //Mark as activated
            student.getLoginId().setSmsToken(null);
            getEntityManager().merge(student);
        } catch (NoResultException e) {
            throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Token does not exist");
        } catch (CustomHttpException ex) {
            throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
    
    public List<Invoice> findInvoices(Integer id) {
        TypedQuery<Invoice> query = getEntityManager().createQuery("SELECT i FROM Invoice i WHERE i.studentCourse.student =:student", Invoice.class);
        query.setParameter("student", new Student(id));
        return query.getResultList();
    }
    
    public Student createStudent(Student entity) throws CustomHttpException {
        Login login = loginFacade.findByEmail(entity.getLoginId().getEmail());
        if (login != null) {
            throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Email already taken");
        }
        super.create(entity);
        return entity;
    }
    
    public BigDecimal getBalance(Student student) throws CustomHttpException {
        BigDecimal balance = new BigDecimal("0");
        try {
            LoginResponse res = WalletUtil.getWalletToken(student.getPhoneNumber(), student.getJpPin());
            balance = new BigDecimal(res.getBalance());
        } catch (IOException ex) {
            // Logger.getLogger(StudentFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return balance;
    }
    
    public Student updateStudent(Student entity) throws CustomHttpException, IllegalAccessException, InvocationTargetException {
        //Check if student is registered and confirmed  
        if (entity.getId() == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student id is required");
        }
        Student managed = super.find(entity.getId());
        if (managed == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student  does not exist");
        }
        super.copy(entity, managed);
        return super.edit(managed);
    }

    //HELPER METHODS
    public List<StudentCourseSitting> getStudentCourseSittings(StudentCourse studentCourse, Registration registration) {
        List<StudentCourseSitting> studentCourseSittings = new ArrayList<>(); //Sittings array 
        if (registration.getExamEntry() != null) {
            Set<StudentCourseSittingPaper> papers = new HashSet<>(); //Sitting papers array 
            Sitting sitting = sittingFacade.findSitting(registration.getExamEntry().getSittingId(), registration.getExamEntry().getYear());
            if (sitting != null) {
                StudentCourseSitting managed = studentCourseSittingFacade.find(studentCourse, sitting);
                if (managed == null) {
                    StudentCourseSitting studentCourseSitting = new StudentCourseSitting(studentCourse, sitting, new ExamCentre(registration.getExamEntry().getCentre().getCode()), StudentCourseSittingStatus.CONFIRMED, null);
                    for (ExamPaper examPaper : registration.getExamEntry().getExamPapers()) {
                        Paper paper = em.find(Paper.class, examPaper.getPaper().getCode());
                        papers.add(new StudentCourseSittingPaper(paper, PaperStatus.PENDING, studentCourseSitting));
                        studentCourseSitting.setPapers(papers);
                    }
                    studentCourseSittings.add(studentCourseSitting);
                }
            }
        } else {//Has no exam entry 
            //if (studentCourse != null && studentCourse.getStudentCourseSittings() != null) {
            for (StudentCourseSitting studentCourseSitting : studentCourse.getStudentCourseSittings()) {
                //Get managed
                StudentCourseSitting managed = studentCourseSittingFacade.find(studentCourseSitting.getId());
                if (managed != null) {
                    managed.setStatus(StudentCourseSittingStatus.PAST);
                    studentCourseSittingFacade.edit(managed);
                }
            }
            // }
        }
        return studentCourseSittings;
    }
    
    public List<Payment> getPayments(StudentCourse studentCourse, Registration registration) throws ParseException {
        List<Payment> payments = new ArrayList<>(); //Receipts array  
        for (Receipt receipt : registration.getReceipts()) {
            List<PaymentDetail> paymentDetails = new ArrayList<>();
            for (ReceiptDetail recDet : receipt.getReceiptDetails()) {
                paymentDetails.add(new PaymentDetail(recDet.getAmount(), recDet.getCategory().getFeeCode(), recDet.getDescription()));
            }
            Payment payment = new Payment(receipt.getReceiptNo(), receipt.getAmount(), receipt.getCurrency().getCode(), receipt.getReferenceNumber(), DateUtil.getDate(receipt.getMdate()));
            payment.setPaymentDetails(paymentDetails);
            payment.setTotalAmount(receipt.getAmount());
            payments.add(payment);
        }
        return payments;
    }
    
    public List<StudentCourseSubscription> getSubscriptions(StudentCourse studentCourse, Registration registration) throws ParseException {
        List<StudentCourseSubscription> subscriptions = new ArrayList<>(); //Subscriptions array
        Integer currentYear = DateUtil.getYear(new Date());
        if (registration.getRenewals() != null && !registration.getRenewals().isEmpty()) {
            for (Renewal renewal : registration.getRenewals()) {  //Renewals
                Date subscriptionExpiry = DateUtil.getDate("30-06-" + renewal.getEndYear());
                StudentCourseSubscriptionPK pk = new StudentCourseSubscriptionPK(studentCourse.getId(), renewal.getEndYear());
                StudentCourseSubscription subscription = new StudentCourseSubscription(renewal.getEndYear(), studentCourse);
                
                subscription.setExpiry(subscriptionExpiry);
                subscription.setStudentCourse(studentCourse);
                subscriptions.add(subscription);
            }
        } else {
            StudentCourseSubscription subscription;
            Date dateRegistered = DateUtil.getDate(registration.getRegistered());
            Integer regYear = DateUtil.getYear(dateRegistered);
            if (dateRegistered.after(DateUtil.getDate("30-06-" + regYear))) {
                Date subscriptionExpiry = DateUtil.getDate("30-06-" + (regYear + 1));
                subscription = new StudentCourseSubscription(DateUtil.getYear(new Date()) + 1, studentCourse);
                subscription.setExpiry(subscriptionExpiry);
            } else {
                Date subscriptionExpiry = DateUtil.getDate("30-06-" + (regYear));
                subscription = new StudentCourseSubscription(DateUtil.getYear(new Date()) + 1, studentCourse);
                subscription.setExpiry(subscriptionExpiry);
            }
            subscription.setStudentCourse(studentCourse);
            subscriptions.add(subscription);
        }
        return subscriptions;
    }
    
    public List<Exemption> getExemptions1(StudentCourse studentCourse, Registration registration) throws ParseException {
        List<Exemption> exemptions = new ArrayList<>(); //Exemptions array
        List<ExemptionPaper> exemptionPapers = new ArrayList<>();
        Exemption exemption = new Exemption();
        for (com.kasneb.client.Exemption coreExemption : registration.getExemptions()) {
            Paper managed = paperFacade.find(coreExemption.getPaperCode());
            if (managed == null) {
                if (coreExemption.getSection() > 6) { //more than one section
                    List<Integer> sectionIds = DigitUtil.split(coreExemption.getSection());
                    for (Integer sectionId : sectionIds) {
                        List<Paper> sectionPapers = paperFacade.findBySection(sectionId, studentCourse.getCourse());
                        for (Paper sectionPaper : sectionPapers) {
                            ExemptionPaper entity = new ExemptionPaper(sectionPaper, new Date(), true, true, VerificationStatus.APPROVED);
                            exemptionPapers.add(entity);
                        }
                    }
                } else {
                    List<Paper> sectionPapers = paperFacade.findBySection(coreExemption.getSection(), studentCourse.getCourse());
                    for (Paper sectionPaper : sectionPapers) {
                        ExemptionPaper entity = new ExemptionPaper(sectionPaper, new Date(), true, true, VerificationStatus.APPROVED);
                        exemptionPapers.add(entity);
                    }
                }
            } else {
                ExemptionPaper entity = new ExemptionPaper(managed, new Date(), true, true, VerificationStatus.APPROVED);
                exemptionPapers.add(entity);
            }

            //exemption.setPapers(exemptionPapers);
            exemption.setStudentCourse(studentCourse);
            exemption.setCreated(new Date());
            exemption.setDateVerified(DateUtil.getDate(coreExemption.getDate()));
            exemption.setStatus(ExemptionStatus.COMPLETED);
            exemption.setReference(coreExemption.getReference());
            exemptions.add(exemption);
        }
        return exemptions;
    }
    
    public List<Paper> getElligiblePapers(StudentCourse studentCourse, Registration registration) throws ParseException {
        List<Paper> eligiblePapers = new ArrayList<>();
        Collection<com.kasneb.client.StudentCoursePaper> coreStudentPapers = registration.getEligiblePapers();
        if (coreStudentPapers != null) {
            for (com.kasneb.client.StudentCoursePaper p : coreStudentPapers) {
                if (p.getPaper() != null) {
                    eligiblePapers.add(new Paper(p.getPaper().getCode()));
                }
            }
        }
        return eligiblePapers;
    }
    
    public Student verifyPreviousStudentCourse(Student entity) throws CustomHttpException, IOException, ParseException {
        Collection<StudentCourse> studentCourses = new ArrayList<>();
        String courseCode = entity.getPreviousCourseCode();
        String sexCode = "1";
        Registration registration = CoreUtil.getStudentCourse(entity.getPreviousRegistrationNo(), new KasnebCourse(courseCode));
        if (registration == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Registration number does not exist");
        }
        
        if (registration.getSex() != null) {
            switch (registration.getSex().getCode()) {
                case "M":
                    sexCode = "1";
                    break;
                case "F":
                    sexCode = "2";
                    break;
            }
        }
        String phoneNumber = registration.getCellphone();
        if (entity.getPhoneNumber() != null) {
            phoneNumber = entity.getPhoneNumber();
        }
        Country country = new Country("1");
        if (registration.getNation() != null) {
            country = new Country(registration.getNation().getCode());
        }
        Student existing = new Student(registration.getFirstName(), registration.getOtherName(), registration.getLastName(), phoneNumber, sexCode, entity.getEmail(), country, DateUtil.getDate(registration.getDateOfBirth()), registration.getIdNumber());
        if (existing == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student does not exist");
        }
        Contact contact = new Contact(registration.getAddress(), registration.getPostalCode(), registration.getTown(), country, null);//String registrationNumber, Boolean active, Date dateVerified, User verifiedBy, String remarks, VerificationStatus verificationStatus, KasnebCourse course, Student student, Sitting firstSitting, Date nextRenewal
        existing.setContact(contact);
        Sitting firstSitting = new Sitting(1);
        StudentCourse studentCourse = new StudentCourse(registration.getRegNo(), true, new Date(), new User(1), "Existing at Kasneb", VerificationStatus.APPROVED, new KasnebCourse(courseCode), firstSitting, true);
        StudentCourseStatus courseStatus = StudentCourseStatus.ACTIVE;
        existing.setCurrentCourse(studentCourse);
        if (registration.getEligiblePapers().isEmpty()) {
            Set<KasnebStudentQualification> qs = new HashSet<>();
            courseStatus = StudentCourseStatus.COMPLETED;
            studentCourse.setActive(false);
            //Set as qualification
            StudentQualificationPK pk = new StudentQualificationPK(existing.getId(), courseCode);
            qs.add(new KasnebStudentQualification(pk));
            existing.setKasnebQualifications(qs);
            existing.setCurrentCourse(null);
        }
        if (entity.getDocumentNo() != null && registration.getIdNumber() != null) {
            if (!entity.getDocumentNo().trim().equals(registration.getIdNumber().trim())) {
                studentCourse.setVerificationStatus(VerificationStatus.PENDING);
                studentCourse.setVerified(Boolean.FALSE);
                courseStatus = StudentCourseStatus.PENDING_IDENTIFICATION;
                studentCourse.setRemarks("Awaiting document verification");
                studentCourse.setVerifiedBy(null);
            }
        } else if (entity.getDob() != null && registration.getDateOfBirth() != null) {
            if (!entity.getDob().equals(registration.getDateOfBirth())) {
                studentCourse.setVerificationStatus(VerificationStatus.PENDING);
                studentCourse.setVerified(Boolean.FALSE);
                courseStatus = StudentCourseStatus.PENDING_IDENTIFICATION;
                studentCourse.setRemarks("Awaiting document verification");
                studentCourse.setVerifiedBy(null);
            }
        }
        List<Payment> payments = new ArrayList<>(); //Receipts array  
        for (Receipt receipt : registration.getReceipts()) {
            List<PaymentDetail> paymentDetails = new ArrayList<>();
            for (ReceiptDetail recDet : receipt.getReceiptDetails()) {
                paymentDetails.add(new PaymentDetail(recDet.getAmount(), recDet.getCategory().getFeeCode(), recDet.getDescription()));
            }
            Payment payment = new Payment(receipt.getReceiptNo(), receipt.getAmount(), receipt.getCurrency().getCode(), receipt.getReferenceNumber(), DateUtil.getDate(receipt.getMdate()));
            payment.setPaymentDetails(paymentDetails);
            payment.setTotalAmount(receipt.getAmount());
            payments.add(payment);
        }
        List<StudentCourseSitting> studentCourseSittings = new ArrayList<>(); //Sittings array 
        if (registration.getExamEntry() != null) {
            Set<StudentCourseSittingPaper> papers = new HashSet<>(); //Sitting papers array 
            Sitting sitting = sittingFacade.findSitting(registration.getExamEntry().getSittingId(), registration.getExamEntry().getYear());
            StudentCourseSitting studentCourseSitting = new StudentCourseSitting(studentCourse, sitting, new ExamCentre(registration.getExamEntry().getCentre().getCode()), StudentCourseSittingStatus.CONFIRMED, null);
            for (ExamPaper examPaper : registration.getExamEntry().getExamPapers()) {
                Paper paper = em.find(Paper.class, examPaper.getPaper().getCode());
                papers.add(new StudentCourseSittingPaper(paper, PaperStatus.PENDING, studentCourseSitting));
                studentCourseSitting.setPapers(papers);
            }
            studentCourseSittings.add(studentCourseSitting);
        }
        studentCourse.setStudentCourseSittings(studentCourseSittings);//Add sittings
        studentCourse.setSubscriptions(getSubscriptions(studentCourse, registration));  //Add subscriptions
        studentCourse.setPayments(payments);  //Add payments
        studentCourse.setElligiblePapers(getElligiblePapers(studentCourse, registration));
        studentCourse.setCourseStatus(courseStatus);//Set status
        studentCourses.add(studentCourse);//Add to collection
        existing.setStudentCourses(studentCourses);
        return existing;
    }
}
