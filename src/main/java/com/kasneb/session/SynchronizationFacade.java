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
import com.kasneb.entity.ExamCentre;
import com.kasneb.entity.KasnebStudentQualification;
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
import com.kasneb.entity.Synchronization;
import com.kasneb.entity.pk.StudentCourseSubscriptionPK;
import com.kasneb.entity.pk.StudentQualificationPK;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.util.CoreUtil;
import com.kasneb.util.DateUtil;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

/**
 *
 * @author jikara
 */
@Stateless
public class SynchronizationFacade extends AbstractFacade<Synchronization> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    @EJB
    com.kasneb.session.StudentFacade studentFacade;
    @EJB
    com.kasneb.session.SittingFacade sittingFacade;
    @EJB
    com.kasneb.session.StudentCourseSittingFacade studentCourseSittingFacade;
    @EJB
    com.kasneb.session.PaperFacade paperFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SynchronizationFacade() {
        super(Synchronization.class);
    }

    public List<Synchronization> getSynchronizations() {
        return super.findAll();
    }

    @Transactional
    @Schedule(hour = "*", minute = "*/10", second = "*", persistent = false)
    public void synchronize() {
        List<Synchronization> synchronizations = getSynchronizations();
        for (Synchronization synchronization : synchronizations) {
            doSynch(synchronization.getId());
        }
    }

    public void doSynch(Integer synchronizationId) {
        Synchronization synchronization = super.find(synchronizationId);
        try {
            Student managed = synchronization.getStudent();
            Collection<StudentCourse> studentCourses = new ArrayList<>();
            StudentCourse currentCourse = managed.getCurrentCourse();
            String sexCode = "1";
            Registration registration = CoreUtil.getStudentCourse(currentCourse.getRegistrationNumber(), currentCourse.getCourse());
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
            if (managed.getPhoneNumber() != null) {
                phoneNumber = managed.getPhoneNumber();
            }
            if (managed == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student does not exist");
            }
            Sitting firstSitting = new Sitting(1);
            StudentCourseStatus courseStatus = StudentCourseStatus.ACTIVE;
            if (registration.getEligiblePapers().isEmpty()) {  //IS COMPELETED
                Set<KasnebStudentQualification> qs = new HashSet<>();
                courseStatus = StudentCourseStatus.COMPLETED;
                managed.getCurrentCourse().setActive(false);
                //Set as qualification
                StudentQualificationPK pk = new StudentQualificationPK(managed.getId(), currentCourse.getCourse().getId());
                qs.add(new KasnebStudentQualification(pk));
                managed.setKasnebQualifications(qs);
                currentCourse.setCourseStatus(courseStatus);
                managed.setCurrentCourse(null);
            }
            currentCourse.setCurrentPart(registration.getCurrentPart());
            currentCourse.setCurrentLevel(registration.getCurrentLevel());
            currentCourse.setCourseStatus(courseStatus);
            currentCourse.setStudentCourseSittings(getStudentCourseSittings(currentCourse, registration));//Add sittings        
            //currentCourse.setExemptions(getExemptions(currentCourse, registration));//Add exemptions       
            currentCourse.setSubscriptions(getSubscriptions(currentCourse, registration));  //Add subscriptions
            currentCourse.setPayments(getPayments(currentCourse, registration));  //Add payments
            currentCourse.setElligiblePapers(getElligiblePapers(currentCourse, registration));
            studentCourses.add(currentCourse);//Add to collection
            managed.setStudentCourses(studentCourses);
            em.merge(managed);
        } catch (CustomHttpException | IOException | ParseException ex) {
            Logger.getLogger(SynchronizationFacade.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            synchronization.setSynched(true);
            super.remove(synchronization);
        }
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

}
