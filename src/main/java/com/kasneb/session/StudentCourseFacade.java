/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.Course;
import com.kasneb.entity.CourseExemption;
import com.kasneb.entity.ElligibleLevel;
import com.kasneb.entity.ElligiblePart;
import com.kasneb.entity.ElligibleSection;
import com.kasneb.entity.ExemptionInvoice;
import com.kasneb.entity.ExemptionInvoiceDetail;
import com.kasneb.entity.Fee;
import com.kasneb.entity.FeeCode;
import com.kasneb.entity.Invoice;
import com.kasneb.entity.InvoiceDetail;
import com.kasneb.entity.InvoiceStatus;
import com.kasneb.entity.KasnebCourse;
import com.kasneb.entity.KasnebStudentCourseQualification;
import com.kasneb.entity.Paper;
import com.kasneb.entity.Paper_;
import com.kasneb.entity.Part;
import com.kasneb.entity.Level;
import com.kasneb.entity.Notification;
import com.kasneb.entity.NotificationStatus;
import com.kasneb.entity.NotificationType;
import com.kasneb.entity.OtherStudentCourseQualification;
import com.kasneb.entity.PaperStatus;
import com.kasneb.entity.Permission;
import com.kasneb.entity.Section;
import com.kasneb.entity.Sitting;
import com.kasneb.entity.SittingPeriod;
import com.kasneb.entity.Student;
import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.StudentCourseQualification;
import com.kasneb.entity.StudentCourseExemptionPaper;
import com.kasneb.entity.StudentCourseSubscription;
import com.kasneb.entity.StudentCourseSittingPaper;
import com.kasneb.entity.StudentCourse_;
import com.kasneb.entity.User;
import com.kasneb.entity.VerificationStatus;
import com.kasneb.entity.pk.StudentCourseExemptionPaperPK;
import com.kasneb.entity.pk.StudentCourseSubscriptionPK;
import com.kasneb.exception.CustomHttpException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
    com.kasneb.session.FeeFacade feeTypeFacade;
    @EJB
    com.kasneb.session.PaperFacade paperFacade;
    @EJB
    com.kasneb.session.LevelFacade levelFacade;
    @EJB
    com.kasneb.session.SystemStatusFacade systemStatusFacade;
    @EJB
    com.kasneb.session.CourseExemptionFacade courseExemptionFacade;

    private static final String REGISTRATION_FEE = "REGISTRATION_FEE";

    private static final String REGISTRATION_RENEWAL_FEE = "REGISTRATION_RENEWAL_FEE";

    private static final String EXEMPTION_FEE = "EXEMPTION_FEE";

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentCourseFacade() {
        super(StudentCourse.class);
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
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student does not exist.");
        }
        if (course == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Course does not exist.");
        }
        if (findByStudentCourse(entity) != null) {
            return findByStudentCourse(entity);
        }
        if (course.getCourseTypeCode() == 100) {
            entity.setCurrentPart(em.find(Part.class, 1));
            entity.setCurrentSection(em.find(Section.class, 1));
        } else if (course.getCourseTypeCode() == 200) {
            entity.setCurrentLevel(new Level(1));
        }
        em.persist(entity);
        return entity;
    }

    public StudentCourse updateStudentCourse(StudentCourse entity) throws CustomHttpException, IllegalAccessException, InstantiationException {
        //Check if student is registered and confirmed  
        if (entity.getId() == null) {
            return createStudentCourse(entity);
        }
        StudentCourse managed = super.find(entity.getId());
        if (managed == null) {
            return createStudentCourse(entity);
        }
        StudentCourseSubscription current = managed.getCurrentSubscription();
        if (current != null && current.getInvoice() != null && current.getInvoice().getStatus().getStatus().equals("PAID")) {
            //Current Suscription has already been verified and course cannot be changed so get by course id
            if (entity.getCourse() != null && !entity.getCourse().equals(managed.getCourse())) {
                //else create a new student course
                return createStudentCourse(entity);
            } else {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Already registered for this course");
            }
        }
        try {
            em.detach(managed);
            super.copy(entity, managed);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(StudentCourseFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
            Invoice invoice = generateRegistrationInvoice(managed);
            //Mark unpaid exemption invoices as null
            Iterator<Invoice> iter = managed.getInvoices().iterator();
            while (iter.hasNext()) {
                Invoice existing = iter.next();
                if (existing.getFeeCode().getCode().equals(REGISTRATION_FEE) && existing.getStatus().getStatus().equals("PENDING")) {
                    iter.remove();
                    existing.setStatus(new InvoiceStatus("CANCELLED"));
                }
            }
            StudentCourseSubscription subscription = new StudentCourseSubscription(new StudentCourseSubscriptionPK(managed.getId(), 2017), getNextRenewalDate(managed), invoice);
            managed.setCurrentSubscription(subscription);
            //Set as current 
            subscription.setCurrent(Boolean.TRUE);
            managed.getSubscriptions().add(subscription);
            Notification notification = new Notification(NotificationStatus.UNREAD, NotificationType.ACTION, "Your registration has expired.", managed.getStudent());
        }
        KasnebCourse dbCourse = em.find(KasnebCourse.class, managed.getCourse().getId());
        if (dbCourse.getCourseType().getCode() == 100) {
            managed.setCurrentPart(em.find(Part.class, 1));
            managed.setCurrentSection(em.find(Section.class, 1));
        } else if (dbCourse.getCourseType().getCode() == 200) {
            managed.setCurrentLevel(new Level(1));
        }
        em.merge(managed);
        return managed;
    }

    public StudentCourse verifyStudentCourse(StudentCourse entity) throws CustomHttpException {
        if (entity.getVerifiedBy() == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Verification failed.Verified by cannot be null");
        }
        if (entity.getId() == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student course id cannot be null");
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
        System.out.println(managed.getCurrentSubscription().getInvoice().getStatus().getStatus());
        if (!managed.getCurrentSubscription().getInvoice().getStatus().getStatus().equals("PAID")) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Verification failed.Student has not paid for this registration");
        }
        if (verifiedBy == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Verification failed.Verifying agent is not defined");
        }
        //check permission
        if (!verifiedBy.getRole().hasPermission(em.find(Permission.class, 100))) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Verification failed.User has no rights to verify this registration");
        }
        //All checks are fine,now verify student
        managed.setVerified(true);
        managed.setVerifiedBy(verifiedBy);
        managed.setDateVerified(new Date());
        managed.setRemarks(entity.getRemarks());
        managed.setActive(true);
        //Generete registration number
        managed.setRegistrationNumber(generateRegistrationNumber(managed));
        managed = em.merge(managed);
        return managed;
    }

    private Invoice generateRegistrationInvoice(StudentCourse studentCourse) throws CustomHttpException {
        KasnebCourse course = em.find(KasnebCourse.class, studentCourse.getCourse().getId());
        Sitting firstSitting = em.find(Sitting.class, studentCourse.getFirstSitting().getId());
        BigDecimal kesTotal = new BigDecimal(0), usdTotal = new BigDecimal(0), gbpTotal = new BigDecimal(0);
        //Generate invoice
        Invoice invoice = new Invoice(UUID.randomUUID().toString(), new Date());
        Fee regFee = feeTypeFacade.getCourseRegistrationFeeType(course);
        invoice.addInvoiceDetail(new InvoiceDetail(regFee.getKesAmount(), regFee.getUsdAmount(), regFee.getGbpAmount(), "Course registration fee"));
        invoice.setStudentCourse(studentCourse);
        //Add to totals
        kesTotal = kesTotal.add(regFee.getKesAmount());
        usdTotal = usdTotal.add(regFee.getUsdAmount());
        gbpTotal = gbpTotal.add(regFee.getGbpAmount());
        if (new Date().after(firstSitting.getRegistrationDeadline())) { //is late
            Fee lateFee = feeTypeFacade.getLateCourseRegistrationFeeType(course);
            invoice.addInvoiceDetail(new InvoiceDetail(lateFee.getKesAmount(), lateFee.getUsdAmount(), lateFee.getGbpAmount(), "Late registration fee"));
            //Add to totals
            kesTotal = kesTotal.add(lateFee.getKesAmount());
            usdTotal = usdTotal.add(lateFee.getUsdAmount());
            gbpTotal = gbpTotal.add(lateFee.getGbpAmount());
        }
        invoice.setKesTotal(kesTotal);
        invoice.setUsdTotal(usdTotal);
        invoice.setGbpTotal(gbpTotal);
        invoice.setFeeCode(new FeeCode(REGISTRATION_FEE));
        return invoice;
    }

    /**
     *
     * @param studentCourse
     * @return
     * @throws CustomHttpException
     */
    public Invoice generateRenewalInvoice(StudentCourse studentCourse) throws CustomHttpException {
        BigDecimal kesTotal = new BigDecimal(0), usdTotal = new BigDecimal(0), gbpTotal = new BigDecimal(0);
        //Generate invoice
        Invoice invoice = new Invoice(UUID.randomUUID().toString(), new Date());
        Fee renewalFee = feeTypeFacade.getAnnualRegistrationRenewalFee(studentCourse.getCourse());
        invoice.addInvoiceDetail(new InvoiceDetail(renewalFee.getKesAmount(), renewalFee.getUsdAmount(), renewalFee.getGbpAmount(), "Annual registration renewal fee"));
        invoice.setStudentCourse(studentCourse);
        //Add to totals
        kesTotal = kesTotal.add(renewalFee.getKesAmount());
        usdTotal = usdTotal.add(renewalFee.getUsdAmount());
        gbpTotal = gbpTotal.add(renewalFee.getGbpAmount());

        invoice.setKesTotal(kesTotal);
        invoice.setUsdTotal(usdTotal);
        invoice.setGbpTotal(gbpTotal);
        invoice.setFeeCode(new FeeCode(REGISTRATION_RENEWAL_FEE));
        return invoice;
    }

    private ExemptionInvoice generateExemptionInvoice(StudentCourse studentCourse) throws CustomHttpException {
        BigDecimal kesTotal = new BigDecimal(0), usdTotal = new BigDecimal(0), gbpTotal = new BigDecimal(0);
        //Generate invoice
        ExemptionInvoice invoice = new ExemptionInvoice(studentCourse.getExemptions(), UUID.randomUUID().toString(), new Date());
        for (Paper paper : studentCourse.getExemptedPapers()) {
            StudentCourseExemptionPaperPK pk = new StudentCourseExemptionPaperPK(studentCourse.getId(), paper.getCode());
            StudentCourseExemptionPaper studentCourseExemptionPaper = new StudentCourseExemptionPaper(pk);
            Fee exemptionFee = feeTypeFacade.getExemptionFee(paper);
            invoice.addExemptionInvoiceDetail(new ExemptionInvoiceDetail(studentCourseExemptionPaper, exemptionFee.getKesAmount(), exemptionFee.getUsdAmount(), exemptionFee.getGbpAmount(), "Exemption fee | " + paper.getCode()));
            //Add to totals
            kesTotal = kesTotal.add(exemptionFee.getKesAmount());
            usdTotal = usdTotal.add(exemptionFee.getUsdAmount());
            gbpTotal = gbpTotal.add(exemptionFee.getGbpAmount());
        }
        invoice.setStudentCourse(studentCourse);
        invoice.setKesTotal(kesTotal);
        invoice.setUsdTotal(usdTotal);
        invoice.setGbpTotal(gbpTotal);
        invoice.setFeeCode(new FeeCode(EXEMPTION_FEE));
        return invoice;
    }

    private Date getNextRenewalDate(StudentCourse entity) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date nextRenewalDate = null;
        Integer sittingYear = entity.getFirstSitting().getSittingYear();
        SittingPeriod sittingPeriod = entity.getFirstSitting().getSittingPeriod();
        Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
        try {
            if (SittingPeriod.MAY == SittingPeriod.MAY && Objects.equals(sittingYear, currentYear)) {
                nextRenewalDate = sdf.parse(currentYear + "-06-30");
            } else if (SittingPeriod.NOVEMBER == SittingPeriod.NOVEMBER) {
                nextRenewalDate = sdf.parse((currentYear + 1) + "-06-30");
            }
        } catch (ParseException ex) {
            Logger.getLogger(StudentCourseFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return nextRenewalDate;
    }

    private String generateRegistrationNumber(StudentCourse entity) {
        return "CPA/1233457/2016";
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
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(Paper.class);
        Root<Paper> partPaper = cq.from(Paper.class);
        cq.where(cb.equal(partPaper.get(Paper_.part), part));
        TypedQuery<Paper> query = em.createQuery(cq);
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

    public Collection getExemptedPapers(StudentCourse studentCourse) {
        Set<Paper> exemptions = new HashSet<>();
        studentCourse.getQualifications().stream().forEach((StudentCourseQualification qualification) -> {
            qualification.getQualification().getCourseExemptions().stream().forEach((c) -> {
                exemptions.add(c.getPaper());
            });
        });
        return exemptions;
    }

    public Collection<Paper> getLevelPapers(Level level) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(Paper.class);
        Root<Paper> elligiblePaper = cq.from(StudentCourseSittingPaper.class);
        TypedQuery<Paper> query = em.createQuery(cq);
        return query.getResultList();
    }

    public ElligiblePart getElligiblePart(StudentCourse studentCourse) {
        Collection<ElligibleSection> elligibleSections = getElligibleSections(studentCourse);
        return new ElligiblePart(studentCourse.getCurrentPart().getName(), elligibleSections);
    }

    public Collection<ElligibleSection> getElligibleSections(StudentCourse studentCourse) {
        Collection<ElligibleSection> elligibleSections = new ArrayList<>();
        for (Section section : studentCourse.getCurrentPart().getSectionCollection()) {
            Collection<Paper> elligiblePapers = getEligiblePapers(section.getPaperCollection(), getExemptedPapers(studentCourse), getPassedPapers(studentCourse));
            elligibleSections.add(new ElligibleSection(section.getName(), elligiblePapers, section.isOptional()));
        }

        return elligibleSections;
    }

    public ElligibleLevel getElligibleLevel(StudentCourse studentCourse) {
        Collection<Paper> elligiblePapers = getEligiblePapers(studentCourse.getCurrentLevel().getPaperCollection(), getExemptedPapers(studentCourse), getPassedPapers(studentCourse));
        return new ElligibleLevel(studentCourse.getCurrentLevel().getName(), elligiblePapers, studentCourse.getCurrentLevel().isOptional());
    }

    public StudentCourse findActive(Integer id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(StudentCourse.class);
        Root<StudentCourse> studentCourse = cq.from(StudentCourse.class);
        cq.where(cb.equal(studentCourse.get(StudentCourse_.id), id), cb.and(cb.equal(studentCourse.get(StudentCourse_.active), true)));
        TypedQuery<StudentCourse> query = em.createQuery(cq);
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ex) {
            return null;
        }
    }

    public List<StudentCourse> findPending() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(StudentCourse.class);
        Root<StudentCourse> studentCourse = cq.from(StudentCourse.class);
        cq.where(cb.equal(studentCourse.get(StudentCourse_.verified), false));
        TypedQuery<StudentCourse> query = em.createQuery(cq);
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
        return query.getSingleResult();
    }

    public StudentCourse findActiveCourse(Student entity) {
        TypedQuery<StudentCourse> query
                = em.createQuery("SELECT s FROM StudentCourse s WHERE s.student =:student AND s.active =:active", StudentCourse.class);
        query.setParameter("active", true);
        query.setParameter("student", entity);
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
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Invoice prepareNextRenewal(StudentCourse active) throws CustomHttpException {
        StudentCourse managed = super.find(active.getId());
        Invoice invoice = generateRenewalInvoice(managed);
        //Integer studentCourseId, Integer year, Date expiry, Invoice invoice
        //Get most recent subscription susbscription
        StudentCourseSubscription subscription = new StudentCourseSubscription(new StudentCourseSubscriptionPK(managed.getId(), 2017), getNextRenewalDate(managed), invoice);
        managed.getSubscriptions().add(subscription);
        em.detach(managed);
        em.merge(managed);
        return invoice;
    }

    public void completeExemption(StudentCourse entity) throws CustomHttpException {
        Integer id = entity.getId();
        StudentCourse managed = super.find(entity.getId());
        if (managed == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student course does not exist");
        }
        Boolean active = managed.getActive();
        Boolean verified = managed.getVerified();
        if (entity.getExemptedPapers() == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Exemption papers must be defined");
        }
        if (entity.getKasnebQualification() == null && entity.getOtherQualification() == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Qualification must be defined");
        }
        //Check if there are any managed qualifications
        Set<Paper> eligibleExemptions = managed.getEligibleExemptions();

        if (entity.getOtherQualification() != null) {
            Course qualification = em.find(Course.class, entity.getOtherQualification().getStudentCourseQualificationPK().getQualificationId());
            if (qualification == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Qualification does not exist");
            }
            for (CourseExemption c : qualification.getCourseExemptions()) {
                eligibleExemptions.add(c.getPaper());
            }
            managed.getOtherQualifications().add(entity.getOtherQualification());
        }
        if (!eligibleExemptions.containsAll(entity.getExemptedPapers())) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Some requested exemptions are not eligible");
        }
        try {
            super.copy(entity, managed);
            em.detach(managed);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(StudentCourseFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        Set<StudentCourseExemptionPaper> studentCourseExemptions = new HashSet<>();
        if (!managed.getKasnebQualifications().isEmpty()) {
            for (KasnebStudentCourseQualification q : managed.getKasnebQualifications()) {
                Collection<CourseExemption> courseExemptions = courseExemptionFacade.findByQualification(q.getQualification());
                for (Paper paper : entity.getExemptedPapers()) {
                    StudentCourseExemptionPaperPK pk = new StudentCourseExemptionPaperPK(managed.getId(), paper.getCode());
                    em.merge(new StudentCourseExemptionPaper(pk, managed, paper, q, false, VerificationStatus.PENDING));
                }
            }
        } else if (!managed.getOtherQualifications().isEmpty()) {
            for (OtherStudentCourseQualification q : managed.getOtherQualifications()) {
                Collection<CourseExemption> courseExemptions = courseExemptionFacade.findByQualification(q.getQualification());
                for (Paper paper : entity.getExemptedPapers()) {
                    StudentCourseExemptionPaperPK pk = new StudentCourseExemptionPaperPK(managed.getId(), paper.getCode());
                    em.merge(new StudentCourseExemptionPaper(pk, managed, paper, q, false, VerificationStatus.PENDING));
                }
            }
        }

        managed.setExemptions(studentCourseExemptions);
        if (entity.getKasnebQualification() != null) {
            //Generate invoice
            ExemptionInvoice inv = generateExemptionInvoice(managed);
            //Mark unpaid exemption invoices as null
            Iterator<Invoice> iter = managed.getInvoices().iterator();
            while (iter.hasNext()) {
                Invoice existing = iter.next();
                if (existing.getFeeCode().getCode().equals(EXEMPTION_FEE) && existing.getStatus().getStatus().equals("PENDING")) {
                    iter.remove();
                    existing.setStatus(new InvoiceStatus("CANCELLED"));
                }
            }
            managed.getInvoices().add(inv);
        }
        managed.setActive(active);
        managed.setVerified(verified);
        em.merge(managed);
    }

    public Set<Paper> getEligibleExemptions(Integer studentCourseId, Integer qualificationId, Integer codeType) throws CustomHttpException {
        if (super.find(studentCourseId) == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student course does not exist");
        }
        return new HashSet<>();
    }

    public List<StudentCourse> findVerificationByUser(User user) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(StudentCourse.class);
        Root<StudentCourse> studentCourse = cq.from(StudentCourse.class);
        cq.where(cb.equal(studentCourse.get(StudentCourse_.verifiedBy), user));
        TypedQuery<StudentCourse> query = em.createQuery(cq);
        return query.getResultList();
    }

}
