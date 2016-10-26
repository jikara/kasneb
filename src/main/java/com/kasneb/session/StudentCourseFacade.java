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
import com.kasneb.entity.Invoice;
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
import com.kasneb.model.BatchStudentCourse;
import com.kasneb.util.CoreUtil;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
    com.kasneb.session.PaperFacade paperFacade;
    @EJB
    com.kasneb.session.LevelFacade levelFacade;
    @EJB
    com.kasneb.session.SystemStatusFacade systemStatusFacade;
    @EJB
    com.kasneb.session.CourseExemptionFacade courseExemptionFacade;
    @EJB
    com.kasneb.session.InvoiceFacade invoiceFacade;

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
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student course does not exist");
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
            Invoice invoice = invoiceFacade.generateRegistrationInvoice(managed);
            StudentCourseSubscription subscription = new StudentCourseSubscription(new StudentCourseSubscriptionPK(managed.getId(), 2017), getNextRenewalDate(managed), invoice);
            managed.setCurrentSubscription(subscription);
            //Set as current 
            subscription.setCurrent(Boolean.TRUE);
            managed.getSubscriptions().add(subscription);
            Notification notification = new Notification(NotificationStatus.UNREAD, NotificationType.DUEDATE, "Your registration has expired.", managed.getStudent());
        }
        KasnebCourse dbCourse = em.find(KasnebCourse.class, managed.getCourse().getId());
        if (dbCourse.getKasnebCourseType().getCode() == 100) {
            managed.setCurrentPart(em.find(Part.class, 1));
            managed.setCurrentSection(em.find(Section.class, 1));
        } else if (dbCourse.getKasnebCourseType().getCode() == 200) {
            managed.setCurrentLevel(new Level(1));
        }
        em.merge(managed);
        return managed;
    }

    public StudentCourse verifyStudentCourse(StudentCourse entity) throws CustomHttpException, IOException {
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
        try {
            em.detach(managed);
            super.copy(entity, managed);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(StudentCourseFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //Generete registration number
        String regNo = generateRegistrationNumber(managed);
        managed.setRegistrationNumber(regNo);
        managed.setVerified(Boolean.TRUE);
        //Create verification notification 
        Notification notification;
        if (managed.getVerificationStatus() == VerificationStatus.APPROVED) {
            notification = new Notification(NotificationStatus.UNREAD, NotificationType.PAYMENT, "Your course registration has been successfully verified.Your registration number is " + regNo, managed.getStudent());
        } else {
            notification = new Notification(NotificationStatus.UNREAD, NotificationType.PAYMENT, "Your course registration has been rejected.Kindly contact Kasneb for further clarification.", managed.getStudent());
        }
        em.persist(notification);
        return em.merge(managed);
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

    private String generateRegistrationNumber(StudentCourse entity) throws IOException, CustomHttpException {
        String registrationNumber = CoreUtil.generateRegistrationNumber(entity);
        return registrationNumber;
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
        studentCourse.getCurrentPart().getSectionCollection().stream().forEach((Section section) -> {
            Collection<Paper> elligiblePapers = getEligiblePapers(section.getPaperCollection(), getExemptedPapers(studentCourse), getPassedPapers(studentCourse));
            elligibleSections.add(new ElligibleSection(section.getName(), elligiblePapers, section.isOptional()));
        });
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
        TypedQuery<StudentCourse> query = em.createQuery("SELECT s FROM StudentCourse s JOIN s.invoices i WHERE i.studentCourse=s AND s.verified =:verified AND i.status = :status", StudentCourse.class);
        query.setParameter("status", new InvoiceStatus("PAID"));
        query.setParameter("verified", false);
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
        Invoice invoice = invoiceFacade.generateRenewalInvoice(managed);
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
            qualification.getCourseExemptions().stream().forEach((CourseExemption c) -> {
                eligibleExemptions.add(c.getPaper());
            });
            managed.getOtherQualifications().add(entity.getOtherQualification());
        }
        if (!eligibleExemptions.containsAll(entity.getExemptedPapers())) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Some requested exemptions are not eligible");
        }
        try {
            em.detach(managed);
            super.copy(entity, managed);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(StudentCourseFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        Set<StudentCourseExemptionPaper> studentCourseExemptions = new HashSet<>();
        if (!managed.getKasnebQualifications().isEmpty()) {
            managed.getKasnebQualifications().stream().map((KasnebStudentCourseQualification q) -> {
                Collection<CourseExemption> courseExemptions = courseExemptionFacade.findByQualification(q.getQualification());
                return q;
            }).forEach((KasnebStudentCourseQualification q) -> {
                entity.getExemptedPapers().stream().forEach((Paper paper) -> {
                    StudentCourseExemptionPaperPK pk = new StudentCourseExemptionPaperPK(managed.getId(), paper.getCode());
                    em.merge(new StudentCourseExemptionPaper(pk, managed, paper, q, false, VerificationStatus.PENDING));
                });
            });
        } else if (!managed.getOtherQualifications().isEmpty()) {
            managed.getOtherQualifications().stream().map((OtherStudentCourseQualification q) -> {
                Collection<CourseExemption> courseExemptions = courseExemptionFacade.findByQualification(q.getQualification());
                return q;
            }).forEach((OtherStudentCourseQualification q) -> {
                entity.getExemptedPapers().stream().forEach((paper) -> {
                    StudentCourseExemptionPaperPK pk = new StudentCourseExemptionPaperPK(managed.getId(), paper.getCode());
                    em.merge(new StudentCourseExemptionPaper(pk, managed, paper, q, false, VerificationStatus.PENDING));
                });
            });
        }

        managed.setExemptions(studentCourseExemptions);
        if (entity.getKasnebQualification() != null) {
            //Generate invoice
            ExemptionInvoice inv = invoiceFacade.generateExemptionInvoice(managed);
            managed.getInvoices().add(inv);
        }
        managed.setActive(active);
        managed.setVerified(verified);
        //Create notification
        Notification notification = new Notification(NotificationStatus.UNREAD, NotificationType.PAYMENT, "Publication fee has been successfully processed", managed.getStudent());
        em.persist(managed);
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

    public void verifyBatchStudentCourse(BatchStudentCourse entity) throws CustomHttpException, IOException {
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
            if (managed == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This student course does not exist");
            }
            if (managed.getVerified()) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Registration already verified");
            }
            if (!managed.getCurrentSubscription().getInvoice().getStatus().getStatus().equals("PAID")) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Verification failed.Student has not paid for this registration");
            }
            //Generete registration number
            String regNo = generateRegistrationNumber(managed);
            managed.setRegistrationNumber(regNo);
            managed.setVerified(Boolean.TRUE);
            //Create verification notification 
            Notification notification;
            if (managed.getVerificationStatus() == VerificationStatus.APPROVED) {
                notification = new Notification(NotificationStatus.UNREAD, NotificationType.PAYMENT, "Your course registration has been successfully verified.Your registration number is " + regNo, managed.getStudent());
            } else {
                notification = new Notification(NotificationStatus.UNREAD, NotificationType.PAYMENT, "Your course registration has been rejected.Kindly contact Kasneb for further clarification.", managed.getStudent());
            }
            em.persist(notification);
            em.merge(managed);
        }
    }

}
