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
import com.kasneb.entity.Fee;
import com.kasneb.entity.FeeCode;
import com.kasneb.entity.Invoice;
import com.kasneb.entity.InvoiceDetail;
import com.kasneb.entity.InvoiceStatus;
import com.kasneb.entity.Paper;
import com.kasneb.entity.Paper_;
import com.kasneb.entity.Part;
import com.kasneb.entity.Level;
import com.kasneb.entity.Notification;
import com.kasneb.entity.NotificationStatus;
import com.kasneb.entity.NotificationType;
import com.kasneb.entity.PaperStatus;
import com.kasneb.entity.Permission;
import com.kasneb.entity.Qualification;
import com.kasneb.entity.Section;
import com.kasneb.entity.Sitting;
import com.kasneb.entity.SittingPeriod;
import com.kasneb.entity.Student;
import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.StudentCourseQualification;
import com.kasneb.entity.StudentCourseSubscription;
import com.kasneb.entity.StudentCourseSittingPaper;
import com.kasneb.entity.StudentCourse_;
import com.kasneb.entity.User;
import com.kasneb.exception.CustomHttpException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
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
        Course course = em.find(Course.class, entity.getCourse().getId());
        if (course.getCourseType() == 100) {
            entity.setCurrentPart(em.find(Part.class, 1));
            entity.setCurrentSection(em.find(Section.class, 1));
        } else if (course.getCourseType() == 200) {
            entity.setCurrentLevel(new Level(1));
        }
        //Set others as inactive
        em.persist(entity);
        return entity;
    }

    public void updateStudentCourse(StudentCourse entity) throws CustomHttpException, IllegalAccessException, InstantiationException {
        //Check if student is registered and confirmed  
        if (entity.getId() == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, systemStatusFacade.getSystemMessage(120));
        }
        StudentCourse managed = super.find(entity.getId());
        StudentCourse toUpdate = new StudentCourse();
        try {
            super.copy(managed, toUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (managed == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, systemStatusFacade.getSystemMessage(104));
        }
        if (managed.getCurrentSubscription() != null && managed.getCurrentSubscription().getInvoice().getStatus().getStatus().equals("PAID")) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, systemStatusFacade.getSystemMessage(104));
        }
        try {
            super.copy(entity, toUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (entity.getFirstSitting() != null) {
            //Check if registration is allowed  
            Sitting firstSitting = em.find(Sitting.class, entity.getFirstSitting().getId());
            if (firstSitting == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, systemStatusFacade.getSystemMessage(103));
            }
            if (new Date().after(firstSitting.getLateRegistrationDeadline())) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, systemStatusFacade.getSystemMessage(101));
            }
            //Create subscription
            Invoice invoice = generateRegistrationInvoice(toUpdate.getStudent(), toUpdate.getCourse(), firstSitting);
            //Integer studentCourseId, Integer year, Date expiry, Invoice invoice
            StudentCourseSubscription subscription = new StudentCourseSubscription(toUpdate, 2017, getNextRenewalDate(entity), invoice);
            subscription.setStudentCourse(managed);
            toUpdate.setCurrentSubscription(subscription);
            //Set as current 
            subscription.setCurrent(Boolean.TRUE);
            toUpdate.getSubscriptions().add(subscription);
            Notification notification = new Notification(NotificationStatus.UNREAD, NotificationType.ACTION, "Your registration has expired.", managed.getStudent());
            //Set next renewal date
            toUpdate.setStudentRequirements(entity.getStudentRequirements());
            toUpdate.setFirstSitting(firstSitting);
        }
        if (toUpdate.getCourse().getCourseType() == 100) {
            toUpdate.setCurrentPart(em.find(Part.class, 1));
            toUpdate.setCurrentSection(em.find(Section.class, 1));
        } else if (entity.getCourse().getCourseType() == 200) {
            toUpdate.setCurrentLevel(new Level(1));
        }
        em.merge(toUpdate);
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
        if (managed.isVerified()) {
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

    private Invoice generateRegistrationInvoice(Student student, Course course, Sitting firstSitting) throws CustomHttpException {
        BigDecimal kesTotal = new BigDecimal(0), usdTotal = new BigDecimal(0), gbpTotal = new BigDecimal(0);
        //Generate invoice
        Invoice invoice = new Invoice(UUID.randomUUID().toString(), new Date());
        Fee regFee = feeTypeFacade.getCourseRegistrationFeeType(course);
        invoice.addInvoiceDetail(new InvoiceDetail(regFee.getKesAmount(), regFee.getUsdAmount(), regFee.getGbpAmount(), "Course registration fee"));
        invoice.setStudent(student);
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

    private Invoice generateRenewalInvoice(Student student, Course course) throws CustomHttpException {
        BigDecimal kesTotal = new BigDecimal(0), usdTotal = new BigDecimal(0), gbpTotal = new BigDecimal(0);
        //Generate invoice
        Invoice invoice = new Invoice(UUID.randomUUID().toString(), new Date());
        Fee renewalFee = feeTypeFacade.getAnnualRegistrationRenewalFee(course);
        invoice.addInvoiceDetail(new InvoiceDetail(renewalFee.getKesAmount(), renewalFee.getUsdAmount(), renewalFee.getGbpAmount(), "Registration annual renewal fee"));
        invoice.setStudent(student);
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

    private Invoice generateExemptionInvoice(Student student, Collection<Paper> exemptedPapers) throws CustomHttpException {
        BigDecimal kesTotal = new BigDecimal(0), usdTotal = new BigDecimal(0), gbpTotal = new BigDecimal(0);
        //Generate invoice
        Invoice invoice = new Invoice(UUID.randomUUID().toString(), new Date());
        for (Paper paper : exemptedPapers) {
            Fee exemptionFee = feeTypeFacade.getExemptionFee(paper);
            invoice.addInvoiceDetail(new InvoiceDetail(exemptionFee.getKesAmount(), exemptionFee.getUsdAmount(), exemptionFee.getGbpAmount(), "Exemption fee"));
            //Add to totals
            kesTotal = kesTotal.add(exemptionFee.getKesAmount());
            usdTotal = usdTotal.add(exemptionFee.getUsdAmount());
            gbpTotal = gbpTotal.add(exemptionFee.getGbpAmount());
        }
        invoice.setStudent(student);
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
            if (SittingPeriod.MAY_JUNE == SittingPeriod.MAY_JUNE && Objects.equals(sittingYear, currentYear)) {
                nextRenewalDate = sdf.parse(currentYear + "-06-30");
            } else if (SittingPeriod.NOVEMBER_DECEMBER == SittingPeriod.NOVEMBER_DECEMBER) {
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
        for (StudentCourseQualification qualification : studentCourse.getQualifications()) {
            for (CourseExemption c : qualification.getQualification().getCourseExemptions()) {
                exemptions.add(c.getPaper());
            }
        }
        return exemptions;
    }

    public Collection getExemptedPapers(Collection<StudentCourseQualification> qualifications) {
        Set<Paper> exemptions = new HashSet<>();
        for (StudentCourseQualification qualification : qualifications) {
            Qualification q = em.find(Qualification.class, qualification.getStudentCourseQualificationPK().getQualificationId());
            for (CourseExemption c : q.getCourseExemptions()) {
                exemptions.add(c.getPaper());
            }
        }
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

    private StudentCourse findByStudentCourse(StudentCourse entity) {
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

    public void prepareNextRenewal(StudentCourse active) throws CustomHttpException {
        StudentCourse managed = super.find(active.getId());
        Invoice invoice = generateRenewalInvoice(managed.getStudent(), managed.getCourse());
        //Integer studentCourseId, Integer year, Date expiry, Invoice invoice
        StudentCourseSubscription subscription = new StudentCourseSubscription(managed, 2017, getNextRenewalDate(managed), invoice);
        managed.getSubscriptions().add(subscription);
        Notification notification = new Notification(NotificationStatus.UNREAD, NotificationType.ACTION, "Your registration has expired.", managed.getStudent());
        em.merge(notification);
        em.merge(managed);
    }

    public void createExemption(StudentCourse entity) throws CustomHttpException {
        StudentCourse managed = super.find(entity.getId());
        if (managed == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student course does not exist");
        }
        StudentCourse toUpdate = new StudentCourse();
        try {
            super.copy(managed, toUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            super.copy(entity, toUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        toUpdate.setQualifications(entity.getQualifications());
        if (toUpdate.getExemptionInvoice() != null) {
            toUpdate.getExemptionInvoice().setStatus(new InvoiceStatus("CANCELLED"));
        }
        Invoice invoice = generateExemptionInvoice(managed.getStudent(), getExemptedPapers(entity.getQualifications()));
        //Create invoice
        toUpdate.setExemptionInvoice(invoice);
        em.merge(toUpdate);
    }

}
