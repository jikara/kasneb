/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.client.LearnAbout;
import com.kasneb.client.Nation;
import com.kasneb.client.Qualification;
import com.kasneb.client.Registration;
import com.kasneb.client.Sex;
import com.kasneb.entity.AlertType;
import com.kasneb.entity.Communication;
import com.kasneb.entity.CommunicationType;
import com.kasneb.entity.Invoice;
import com.kasneb.entity.InvoiceStatus;
import com.kasneb.entity.KasnebCourse;
import com.kasneb.entity.Paper;
import com.kasneb.entity.Paper_;
import com.kasneb.entity.Part;
import com.kasneb.entity.Level;
import com.kasneb.entity.LevelPK;
import com.kasneb.entity.Notification;
import com.kasneb.entity.NotificationStatus;
import com.kasneb.entity.NotificationType;
import com.kasneb.entity.PaperStatus;
import com.kasneb.entity.Permission;
import com.kasneb.entity.Section;
import com.kasneb.entity.Sitting;
import com.kasneb.entity.SittingPeriod;
import com.kasneb.entity.Student;
import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.StudentCourseSubscription;
import com.kasneb.entity.StudentCourseSittingPaper;
import com.kasneb.entity.StudentCourseStatus;
import com.kasneb.entity.StudentCourse_;
import com.kasneb.entity.User;
import com.kasneb.entity.VerificationStatus;
import com.kasneb.entity.pk.PartPK;
import com.kasneb.entity.pk.StudentCourseSubscriptionPK;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.model.BatchStudentCourse;
import com.kasneb.util.CoreUtil;
import static com.kasneb.util.CoreUtil.getFirstExemDate;
import com.kasneb.util.DateUtil;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.Response;

/**
 *
 * @author jikara
 */
@Stateless
public class StudentCourseFacade extends AbstractFacade<StudentCourse> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    @EJB
    com.kasneb.session.PaperFacade paperFacade;
    @EJB
    com.kasneb.session.LevelFacade levelFacade;
    @EJB
    com.kasneb.session.SystemStatusFacade systemStatusFacade;
    @EJB
    com.kasneb.session.CourseExemptionFacade courseExemptionFacade;
    @EJB
    com.kasneb.session.InvoiceFacade invoiceFacade;
    @EJB
    com.kasneb.session.ExemptionFacade studentCourseExemptionFacade;
    @EJB
    com.kasneb.session.StudentFacade studentFacade;
    @EJB
    com.kasneb.session.PaymentFacade paymentFacade;
    @EJB
    com.kasneb.session.CommunicationFacade communicationFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentCourseFacade() {
        super(StudentCourse.class);
    }

    @Override
    public List<StudentCourse> findAll() {
        TypedQuery<StudentCourse> query = em.createQuery("SELECT s FROM StudentCourse s", StudentCourse.class);
        query.setMaxResults(100);
        return query.getResultList();
    }

    public List<StudentCourse> findAll(Integer userId) {
        TypedQuery<StudentCourse> query = em.createQuery("SELECT s FROM StudentCourse s WHERE s.verifiedBy =:user ORDER BY s.dateVerified DESC", StudentCourse.class);
        query.setParameter("user", new User(userId));
        query.setMaxResults(100);
        return query.getResultList();
    }

    public List<StudentCourse> findAll(Date startDate, Date endDate) {
        TypedQuery<StudentCourse> query = em.createQuery("SELECT s FROM StudentCourse s WHERE s.created BETWEEN :startDate AND :endDate ORDER BY s.dateVerified DESC", StudentCourse.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setMaxResults(100);
        return query.getResultList();
    }

    public List<StudentCourse> findAll(Date startDate, Date endDate, Integer userId) {
        TypedQuery<StudentCourse> query = em.createQuery("SELECT s FROM StudentCourse s WHERE s.verifiedBy =:user AND s.created BETWEEN :startDate AND :endDate ORDER BY s.dateVerified DESC", StudentCourse.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("user", new User(userId));
        query.setMaxResults(10);
        return query.getResultList();
    }

    public StudentCourse createStudentCourse(StudentCourse entity) throws CustomHttpException {
        if (entity.getStudent() == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student cannot be null.");
        }
        if (entity.getCourse() == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Course cannot be null.");
        }
        KasnebCourse course = em.find(KasnebCourse.class, entity.getCourse().getId());
        Student student = em.find(Student.class, entity.getStudent().getId());
        if (student == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student id does not exist.");
        }
        if (student.getCurrentCourse() != null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student already has an active course");
        }
        if (student == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student does not exist.");
        }
        if (course == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Course does not exist.");
        }
        if (findByStudentCourse(entity) != null) {
            return findByStudentCourse(entity);
        }
        entity.setActive(Boolean.FALSE);
        entity.setVerificationStatus(VerificationStatus.PENDING);
        em.persist(entity);
        return entity;
    }

    public StudentCourse updateStudentCourse(StudentCourse entity) throws CustomHttpException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException {
        StudentCourse managed = super.find(entity.getId());
        if (managed == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student course does not exist");
        }
        StudentCourseSubscription last = managed.getLastSubscription();
        if (last != null && last.getInvoice() != null && last.getInvoice().getStatus().getStatus().equals("PAID")) {
            //Current Suscription has already been verified and course cannot be changed so get by course id
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This course registration has already been paid for and cannot be updated.");

        }
        em.detach(managed);
        super.copy(entity, managed);
        if (managed.getFirstSitting() != null) {
            //Check if registration is allowed  
            Sitting firstSitting = em.find(Sitting.class, managed.getFirstSitting().getId());
            if (firstSitting == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, systemStatusFacade.getSystemMessage(103));
            }
            if (new Date().after(firstSitting.getLateRegistrationDeadline())) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, systemStatusFacade.getSystemMessage(101));
            }
            //Create subscription 
            Invoice invoice = invoiceFacade.generateRegistrationInvoice(managed);
            StudentCourseSubscription subscription = new StudentCourseSubscription(new StudentCourseSubscriptionPK(managed.getId(), DateUtil.getYear(new Date()) + 1));
            subscription.setExpiry(getNextRenewalDate(managed));
            subscription.setInvoice(invoice);
            em.merge(subscription);
            // managed.setSubscriptions(subscriptions);
            Notification notification = new Notification(NotificationStatus.UNREAD, NotificationType.DUEDATE, "Your registration has expired.", managed.getStudent());
        }
        KasnebCourse dbCourse = em.find(KasnebCourse.class, managed.getCourse().getId());
        switch (dbCourse.getCourseTypeCode()) {
            case 100:
                Part part = em.find(Part.class, new PartPK(1, dbCourse.getId()));
                managed.setCurrentPart(part);
                break;
            case 200:
                Level level = em.find(Level.class, new LevelPK(1, dbCourse.getId()));
                managed.setCurrentLevel(level);
                break;
        }
        return super.edit(managed);
    }

    public StudentCourse updateStudentCourseDocuments(StudentCourse entity) throws CustomHttpException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException {
        StudentCourse managed = super.find(entity.getId());
        super.copy(entity, managed);
        managed = em.merge(managed);
        return managed;
    }

    public StudentCourse verifyStudentCourse(StudentCourse entity) throws CustomHttpException, IOException, MessagingException, ParseException, IllegalAccessException, InvocationTargetException {
        if (entity.getVerifiedBy() == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Verification failed.Verified by cannot be null");
        }
        if (entity.getId() == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student course id cannot be null");
        }
        if (entity.getVerificationStatus() == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Verification status cannot be null");
        }
        //Check if student is registered
        StudentCourse managed = em.find(StudentCourse.class, entity.getId());
        User verifiedBy = em.find(User.class, entity.getVerifiedBy().getId());
        if (managed == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Verification failed.Student is not registered for this course");
        }
        if (managed.getVerified()) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Registration already verified");
        }
        if (!managed.getLastSubscription().getInvoice().getStatus().getStatus().equals("PAID")) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Verification failed.Student has not paid for this registration");
        }
        if (verifiedBy == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Verification failed.Verifying agent is not defined");
        }
        //check permission
//        if (!verifiedBy.getRole().hasPermission(em.find(Permission.class, 100))) {
//            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Verification failed.User has no rights to verify this registration");
//        }
        //All checks are fine,now verify student
        super.copy(entity, managed);
        //Generate registration number
        if (entity.getVerificationStatus().equals(VerificationStatus.APPROVED)) {
            Registration registration = createRegistration(managed);
            Integer regNo = registration.getRegNo();
            managed.setRegistrationNumber(regNo);
            managed.setCourseStatus(StudentCourseStatus.ACTIVE);
            managed.setActive(Boolean.TRUE);
            Student student = managed.getStudent();
            student.setCurrentCourse(managed);
            studentFacade.edit(student);
        } else {
            managed.setCourseStatus(StudentCourseStatus.REJECTED);
            switch (managed.getRejectCode()) {
                case "REJ_1":
                    //Not Qualified for course Applied For,refund
                    Communication communication = new Communication(managed.getStudent(), managed, null, null, CommunicationType.REFUND_REQUEST, AlertType.EMAIL, false);
                    communicationFacade.create(communication);//Email
                    break;
                case "REJ_2":
                    //Mismatch of names on supporting Document and Identification Document,reattach
                    break;
                case "REJ_3":
                    //Already an Existing KASNEB student(Allow Typing of Registration Number and Amount
                    break;
                case "REJ_4":
                    //Lack of Supporting Documents( Allow type of Missing Document,reattach
                    break;
                case "REJ_5":
                    //Not Qualified for course Applied For
                    communication = new Communication(managed.getStudent(), managed, null, null, CommunicationType.REFUND_REQUEST, AlertType.EMAIL, false);
                    communicationFacade.create(communication);//Email
                    communication.setAlertType(AlertType.SMS);
                    break;
            }
        }
        managed.setVerified(Boolean.TRUE);
        //Create verification notification 
        Communication smsCommunication = new Communication(managed.getStudent(), managed, null, null, CommunicationType.COURSE_VERIFICATION, AlertType.SMS, false);
        communicationFacade.create(smsCommunication);
        Communication emailCommunication = new Communication(managed.getStudent(), managed, null, null, CommunicationType.COURSE_VERIFICATION, AlertType.EMAIL, false);
        communicationFacade.create(emailCommunication);
        return em.merge(managed);
    }

    private Registration createRegistration(StudentCourse studentCourse) throws ParseException, IOException, CustomHttpException {
        Student student = studentCourse.getStudent();
        com.kasneb.client.Course previousCourse = new com.kasneb.client.Course("00");
        if (student.getPreviousCourseCode() != null) {
            previousCourse = new com.kasneb.client.Course(student.getPreviousCourseCode());
        }
        String learnAbout = "";
        if (studentCourse.getLearnAbout() != null) {
            learnAbout = studentCourse.getLearnAbout().getSpecification();
        }
        String sex = "M";
        switch (student.getGender()) {
            case "1":
                sex = "M";
                break;
            case "2":
                sex = "F";
                break;
        }
        Registration registration = new Registration(
                null, //regNo
                "",//registrationNumber
                DateUtil.getString(student.getCreated()), //registered
                getFirstExemDate(studentCourse.getFirstSitting()), //firstExamDate
                student.getLastName(), //lastName
                studentCourse.getStudent().getFirstName(), //firstName
                student.getMiddleName(), //otherName
                "", //otherName2
                new Sex(sex), //Sex 
                DateUtil.getString(student.getDateOfBirth()), //dateOfBirth
                student.getDocumentNo(),//idNumber
                new Qualification(1), //quali
                DateUtil.getString(new Date()), //rrDAre
                "", //rrNumber
                student.getPreviousRegistrationNo() + "", //pReg
                student.getContact().getPostalAddress(), //physicalAddress
                student.getContact().getPostalAddress(),//address
                student.getContact().getTown(),//town
                student.getContact().getCountryId().getName(),//country
                student.getContact().getPostalCode(),//postalCode
                student.getEmail(), //email
                student.getPhoneNumber(), //Cell phone
                "", //Telephone
                previousCourse, //previousCourse
                learnAbout, //Learn about
                new LearnAbout(4), //learnt 
                new Nation(student.getCountryId().getCode()), //Nationality
                new Qualification(5),
                null,//receipts
                null,//eligiblePapers
                null,//examBookings
                null//cpaExamEntry
        );
        return CoreUtil.createCoreRegistration(registration, studentCourse.getCourse());
    }

    private Date getNextRenewalDate(StudentCourse entity) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date nextRenewalDate = null;
        Integer sittingYear = entity.getFirstSitting().getSittingYear();
        SittingPeriod sittingPeriod = entity.getFirstSitting().getSittingPeriod();
        Integer currentYear = DateUtil.getYear(new Date());
        try {
            if (SittingPeriod.MAY == SittingPeriod.MAY && Objects.equals(sittingYear, currentYear)) {
                nextRenewalDate = sdf.parse("30-06-" + currentYear);
            } else if (SittingPeriod.NOVEMBER == SittingPeriod.NOVEMBER) {
                nextRenewalDate = sdf.parse("30-06-" + (currentYear + 1));
            }
        } catch (ParseException ex) {
            // Logger.getLogger(StudentCourseFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return nextRenewalDate;
    }

    public Collection<Paper> getPassedPapers(StudentCourse studentCourse) {
        TypedQuery<Paper> query
                = em.createQuery("SELECT p FROM Paper p JOIN p.studentCourseSittingPaper sp WHERE sp.studentCourseSitting.studentCourse=:studentCourse AND sp.paperStatus =:paperStatus", Paper.class);
        query.setParameter("studentCourse", studentCourse);
        query.setParameter("paperStatus", PaperStatus.PASS);
        return query.getResultList();
    }

    public Collection<Paper> getEligiblePapers(Collection<Paper> papers, Collection<Paper> exempted, Collection<Paper> passedPapers) {
        papers.removeAll(exempted); //remove exempted
        papers.removeAll(passedPapers); //remove passed
        return papers;
    }

    public Collection<Paper> getPartPapers(Part part) {
        TypedQuery<Paper> query
                = em.createQuery("SELECT p FROM Paper p WHERE p.section.part =:part", Paper.class);
        query.setParameter("part", part);
        return query.getResultList();
    }

    public Collection getSectionPapers(Section section) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(Paper.class);
        Root<Paper> partPaper = cq.from(Paper.class);
        cq.where(cb.equal(partPaper.get(Paper_.section), section));
        TypedQuery<Paper> query = em.createQuery(cq);
        return query.getResultList();
    }

    public Collection<Paper> getLevelPapers(Level level) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(Paper.class);
        Root<Paper> elligiblePaper = cq.from(StudentCourseSittingPaper.class);
        TypedQuery<Paper> query = em.createQuery(cq);
        return query.getResultList();
    }

    public StudentCourse findActive(Integer id) {
        try {
            TypedQuery<StudentCourse> query = em.createQuery("select s from StudentCourse s left join fetch s.elligiblePapers WHERE s.id =:id", StudentCourse.class);
            query.setParameter("id", id);
            StudentCourse studentCourse = query.getSingleResult();
            return studentCourse;
        } catch (javax.persistence.NoResultException ex) {
            return null;
        }
    }

    public List<StudentCourse> findPending() {
        TypedQuery<StudentCourse> query = em.createQuery("SELECT DISTINCT s FROM StudentCourse s JOIN s.invoices i WHERE i.studentCourse=s AND s.verified =:verified AND i.status = :status", StudentCourse.class);
        query.setParameter("status", new InvoiceStatus("PAID"));
        query.setParameter("verified", false);
        query.setMaxResults(20);
        return query.getResultList();
    }

    public List<StudentCourse> findPendingIdentification() {
        TypedQuery<StudentCourse> query = em.createQuery("SELECT s FROM StudentCourse s LEFT JOIN fetch s.student WHERE s.courseStatus =:courseStatus ORDER BY s.id DESC", StudentCourse.class);
        query.setParameter("courseStatus", StudentCourseStatus.PENDING_IDENTIFICATION);
        query.setMaxResults(20);
        return query.getResultList();
    }

    public List<StudentCourse> findVerified() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(StudentCourse.class);
        Root<StudentCourse> studentCourse = cq.from(StudentCourse.class);
        cq.where(cb.equal(studentCourse.get(StudentCourse_.verified), true));
        TypedQuery<StudentCourse> query = em.createQuery(cq);
        return query.getResultList();
    }

    public StudentCourse findStudentCourse(Integer id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(StudentCourse.class);
        Root<StudentCourse> studentCourse = cq.from(StudentCourse.class);
        cq.where(cb.equal(studentCourse.get(StudentCourse_.id), id));
        TypedQuery<StudentCourse> query = em.createQuery(cq);
        query.setMaxResults(1);
        return query.getSingleResult();
    }

    public StudentCourse findActiveCourse(Student entity) {
        TypedQuery<StudentCourse> query
                = em.createQuery("SELECT s FROM StudentCourse s WHERE s.student =:student AND s.active =:active", StudentCourse.class);
        query.setParameter("active", true);
        query.setParameter("student", entity);
        query.setMaxResults(1);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (javax.persistence.NonUniqueResultException e) {
            return query.getResultList().get(0);
        }
    }

    public StudentCourse findByStudentCourse(StudentCourse entity) {
        TypedQuery<StudentCourse> query
                = em.createQuery("SELECT s FROM StudentCourse s WHERE s.student =:student AND s.course =:course", StudentCourse.class);
        query.setParameter("course", entity.getCourse());
        query.setParameter("student", entity.getStudent());
        query.setMaxResults(1);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Paper> getEligibleExemptionsByQualification(StudentCourse studentCourse, String qualificationId, Integer codeType) throws CustomHttpException {
        TypedQuery<Paper> query = em.createQuery("SELECT c.paper FROM CourseExemption c WHERE c.qualification.id =:qualificationId AND c.course.id =:courseCode", Paper.class);
        query.setParameter("qualificationId", qualificationId);
        query.setParameter("courseCode", studentCourse.getCourse().getId());
        List<Paper> papers = query.getResultList();
        return papers;
    }

    public List<Paper> getEligibleExemptions(Integer studentCourseId, List<String> qualificationIds) throws CustomHttpException {
        StudentCourse studentCourse = super.find(studentCourseId);
        Set<Paper> papers = new HashSet<>();
        List<Paper> papersList = new ArrayList<>();
        for (String qualificationId : qualificationIds) {
            List<Paper> papersByQualification = getEligibleExemptionsByQualification(studentCourse, qualificationId, 1);
            papers.addAll(papersByQualification);
        }
        papersList.addAll(papers);
        papers.removeAll(studentCourse.getExemptedPapers());
        Collections.sort(papersList, (Paper p1, Paper p2) -> p1.getCode().compareTo(p2.getCode()));
        return papersList;
    }

    public List<StudentCourse> findVerificationByUser(User user) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(StudentCourse.class);
        Root<StudentCourse> studentCourse = cq.from(StudentCourse.class);
        cq.where(cb.equal(studentCourse.get(StudentCourse_.verifiedBy), user));
        TypedQuery<StudentCourse> query = em.createQuery(cq);
        return query.getResultList();
    }

    public void verifyBatchStudentCourse(BatchStudentCourse entity) throws CustomHttpException, IOException, MessagingException, ParseException {
        if (entity.getVerifiedBy() == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Verification failed.Verified by cannot be null");
        }
        User verifiedBy = em.find(User.class, entity.getVerifiedBy());
        if (verifiedBy == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Verification failed.Verifying agent is not defined");
        }
        //check permission
        if (!verifiedBy.getRole().hasPermission(em.find(Permission.class, 100))) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Verification failed.User has no rights to verify this registration");
        }
        for (StudentCourse studentCourse : entity.getStudentCourses()) {
            //Check if student is registered
            StudentCourse managed = em.find(StudentCourse.class, studentCourse.getId());
            em.detach(managed);
            if (managed == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This student course does not exist");
            }
            if (managed.getVerified()) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Registration already verified");
            }
            if (!managed.getLastSubscription().getInvoice().getStatus().getStatus().equals("PAID")) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Verification failed.Student has not paid for this registration");
            }
            //Generete registration number     
            Registration registration = createRegistration(managed);
            Integer regNo = registration.getRegNo();
            managed.setRegistrationNumber(regNo);
            managed.setVerified(Boolean.TRUE);
            managed.setActive(Boolean.TRUE);
            managed.setCourseStatus(StudentCourseStatus.ACTIVE);
            //Create verification notification 
            Communication smsCommunication = new Communication(managed.getStudent(), managed, null, null, CommunicationType.COURSE_VERIFICATION, AlertType.SMS, false);
            communicationFacade.create(smsCommunication);
            Communication emailCommunication = new Communication(managed.getStudent(), managed, null, null, CommunicationType.COURSE_VERIFICATION, AlertType.EMAIL, false);
            communicationFacade.create(emailCommunication);
            em.merge(managed);
        }
    }

    public List<StudentCourse> findVerifications(Integer userId) {
        TypedQuery<StudentCourse> query = em.createQuery("SELECT s FROM StudentCourse s WHERE s.verifiedBy.id =:userId ORDER BY s.dateVerified DESC", StudentCourse.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public List<StudentCourse> findVerifications(Integer userId, Date startDate, Date endDate) {
        TypedQuery<StudentCourse> query = em.createQuery("SELECT s FROM StudentCourse s WHERE s.dateVerified BETWEEN :startDate AND :endDate ORDER BY s.dateVerified DESC", StudentCourse.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setMaxResults(20);
        return query.getResultList();
    }

    public Boolean registrationExists(Student entity) {
        try {
            TypedQuery<StudentCourse> query = em.createQuery("SELECT s FROM StudentCourse s WHERE s.registrationNumber =:registrationNumber AND s.course.id =:courseId AND s.verificationStatus =:verificationStatus", StudentCourse.class);
            query.setParameter("registrationNumber", entity.getPreviousRegistrationNo());
            query.setParameter("courseId", entity.getPreviousCourseCode());
            query.setParameter("verificationStatus", VerificationStatus.APPROVED);
            query.setMaxResults(1);
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public StudentCourse identify(StudentCourse managed) {
        if (managed.getCourseStatus().equals(StudentCourseStatus.ACTIVE)) {
            managed.setVerified(Boolean.TRUE);
            managed.setActive(Boolean.TRUE);
            managed.setVerificationStatus(VerificationStatus.APPROVED);
            managed.setRemarks("Approved by administrator");
            super.edit(managed);
            Communication communication = new Communication(managed.getStudent(), managed, null, null, CommunicationType.IDENTIFICATION, AlertType.SMS, false);
            communicationFacade.create(communication);
            communication.setAlertType(AlertType.EMAIL);
            communicationFacade.create(communication);
        } else if (managed.getCourseStatus().equals(StudentCourseStatus.FAILED_IDENTIFICATION)) {
            Communication communication = new Communication(managed.getStudent(), managed, null, null, CommunicationType.IDENTIFICATION, AlertType.SMS, false);
            communicationFacade.create(communication);
            communication.setAlertType(AlertType.EMAIL);
            communicationFacade.create(communication);
        }
        return managed;
    }
}
