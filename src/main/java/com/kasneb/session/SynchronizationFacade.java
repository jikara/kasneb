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
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    com.kasneb.session.StudentCourseFacade studentCourseFacade;
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

    public void doSynch(Student managed) {
        try {
            String sexCode = "1";
            Registration registration = CoreUtil.getStudentCourse(managed.getCurrentCourse().getRegistrationNumber(), managed.getCurrentCourse().getCourse());
            if (registration == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Registration number does not exist");
            }
            if (registration.getSex() != null) {
                sexCode = registration.getSex().getCode().equals("M") ? "1" : "2";
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
                StudentQualificationPK pk = new StudentQualificationPK(managed.getId(), managed.getCurrentCourse().getCourse().getId());
                qs.add(new KasnebStudentQualification(pk));
                managed.setKasnebQualifications(qs);
                managed.getCurrentCourse().setCourseStatus(courseStatus);
                managed.setCurrentCourse(null);
            } else {
                if (registration.getCurrentPart() != null) {
                    managed.getCurrentCourse().setCurrentPartId(registration.getCurrentPart().getId());
                }
                if (registration.getCurrentLevel() != null) {
                    managed.getCurrentCourse().setCurrentLevelId(registration.getCurrentLevel().getId());
                }
                managed.getCurrentCourse().setElligiblePapers(new ArrayList<>());
                managed.getCurrentCourse().setCourseStatus(courseStatus);
                this.updateStudentCourseSittings(managed.getCurrentCourse(), registration);//Add sittings        
                //currentCourse.setExemptions(getExemptions(currentCourse, registration));//Add exemptions       
                this.updateSubscriptions(managed.getCurrentCourse(), registration);  //Add subscriptions
                this.updatePayments(managed.getCurrentCourse(), registration);  //Add payments  
                List<Paper> papers = getElligiblePapers(managed.getCurrentCourse(), registration); //Elligible Papers
                managed.getCurrentCourse().setElligiblePapers(papers);
                em.merge(managed.getCurrentCourse());
            }
        } catch (ParseException | CustomHttpException | IOException ex) {
            Logger.getLogger(SynchronizationFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public List<Paper> getElligiblePapers(StudentCourse studentCourse, Registration registration) throws ParseException {
        Collection<com.kasneb.client.StudentCoursePaper> coreStudentPapers = registration.getEligiblePapers();
        List<Paper> papers = new ArrayList<>();
        if (coreStudentPapers != null) {
            for (com.kasneb.client.StudentCoursePaper p : coreStudentPapers) {
                if (p.getPaper() != null) {
                    Paper paper = paperFacade.findPaper(p.getPaper().getCode());
                    papers.add(paper);
                }
            }
        }
        return papers;
    }

    public void updateStudentCourseSittings(StudentCourse studentCourse, Registration registration) {
        List<StudentCourseSitting> studentCourseSittings = new ArrayList<>(); //Sittings array 
        if (registration.getExamEntry() != null) {
            Set<StudentCourseSittingPaper> papers = new HashSet<>(); //Sitting papers array 
            Sitting sitting = sittingFacade.findSitting(registration.getExamEntry().getSittingId(), registration.getExamEntry().getYear());
            if (sitting != null) {
                StudentCourseSitting managed = studentCourseSittingFacade.find(studentCourse, sitting);
                if (managed == null) {
                    StudentCourseSitting studentCourseSitting = new StudentCourseSitting(studentCourse, sitting, new ExamCentre(registration.getExamEntry().getCentre().getCode()), StudentCourseSittingStatus.CONFIRMED, null);
                    for (ExamPaper examPaper : registration.getExamEntry().getExamPapers()) {
                        Paper paper = paperFacade.findPaper(examPaper.getPaper().getCode());
                        StudentCourseSittingPaper StudentCourseSittingPaper = new StudentCourseSittingPaper(paper, PaperStatus.PENDING, studentCourseSitting);
                        papers.add(StudentCourseSittingPaper);
                        // studentCourseSitting.setPapers(papers);
                    }
                    em.merge(studentCourseSitting);
                }
            }
        } else {//Has no exam entry 
            List<StudentCourseSitting> sittings = studentCourseSittingFacade.findSittings(studentCourse);
            for (StudentCourseSitting studentCourseSitting : sittings) {
                //Get managed
                StudentCourseSitting managed = studentCourseSittingFacade.find(studentCourseSitting.getId());
                if (managed != null) {
                    if (managed.getInvoice() != null && managed.getInvoice().getStatus().getStatus().equals("PAID") && managed.getSittingCentre() == null) {
                        managed.setStatus(StudentCourseSittingStatus.PAID);
                    } else {
                        managed.setStatus(StudentCourseSittingStatus.PAST);
                    }
                    studentCourseSittingFacade.edit(managed);
                }
            }
        }
    }

    public void updatePayments(StudentCourse studentCourse, Registration registration) throws ParseException {
        for (Receipt receipt : registration.getReceipts()) {
            List<PaymentDetail> paymentDetails = new ArrayList<>();
            for (ReceiptDetail recDet : receipt.getReceiptDetails()) {
                paymentDetails.add(new PaymentDetail(recDet.getAmount(), recDet.getCategory().getFeeCode(), recDet.getDescription()));
            }
            Payment payment = new Payment(receipt.getReceiptNo(), receipt.getAmount(), receipt.getCurrency().getCode(), receipt.getReferenceNumber(), DateUtil.getDate(receipt.getMdate()));
            payment.setPaymentDetails(paymentDetails);
            payment.setTotalAmount(receipt.getAmount());
            em.merge(payment);
        }
    }

    public void updateSubscriptions(StudentCourse studentCourse, Registration registration) throws ParseException {
        List<StudentCourseSubscription> subscriptions = new ArrayList<>(); //Subscriptions array
        Integer currentYear = DateUtil.getYear(new Date());
        if (registration.getRenewals() != null && !registration.getRenewals().isEmpty()) {
            for (Renewal renewal : registration.getRenewals()) {  //Renewals
                Date subscriptionExpiry = DateUtil.getDate("30-06-" + renewal.getEndYear());
                StudentCourseSubscriptionPK pk = new StudentCourseSubscriptionPK(studentCourse.getId(), renewal.getEndYear());
                StudentCourseSubscription subscription = new StudentCourseSubscription(renewal.getEndYear(), studentCourse);
                subscription.setPk(pk);
                subscription.setExpiry(subscriptionExpiry);
                subscription.setStudentCourse(studentCourse);
                em.merge(subscription);
            }
        } else {
            StudentCourseSubscription subscription;
            Date dateRegistered = DateUtil.getDate(registration.getRegistered());
            Integer regYear = DateUtil.getYear(dateRegistered);
            if (dateRegistered.after(DateUtil.getDate("30-06-" + regYear))) {
                Date subscriptionExpiry = DateUtil.getDate("30-06-" + (regYear + 1));
                subscription = new StudentCourseSubscription(DateUtil.getYear(new Date()) + 1, studentCourse);
                subscription.setExpiry(subscriptionExpiry);
                StudentCourseSubscriptionPK pk = new StudentCourseSubscriptionPK(studentCourse.getId(), DateUtil.getYear(new Date()) + 1);
                subscription.setPk(pk);
            } else {
                Date subscriptionExpiry = DateUtil.getDate("30-06-" + (regYear));
                subscription = new StudentCourseSubscription(DateUtil.getYear(new Date()), studentCourse);
                subscription.setExpiry(subscriptionExpiry);
                StudentCourseSubscriptionPK pk = new StudentCourseSubscriptionPK(studentCourse.getId(), DateUtil.getYear(new Date()));
                subscription.setPk(pk);
            }
            subscription.setStudentCourse(studentCourse);
            em.merge(subscription);
        }
    }
}
