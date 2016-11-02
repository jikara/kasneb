/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.ExemptionInvoice;
import com.kasneb.entity.Paper;
import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.StudentCourseExemptionPaper;
import com.kasneb.entity.User;
import com.kasneb.entity.VerificationStatus;
import com.kasneb.entity.pk.StudentCourseExemptionPaperPK;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.model.Email;
import com.kasneb.model.Exemption;
import com.kasneb.util.EmailUtil;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;

/**
 *
 * @author jikara
 */
@Stateless
public class StudentCourseExemptionFacade extends AbstractFacade<StudentCourseExemptionPaper> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    @EJB
    com.kasneb.session.InvoiceFacade invoiceFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentCourseExemptionFacade() {
        super(StudentCourseExemptionPaper.class);
    }

    public List<StudentCourseExemptionPaper> findPending() {
        TypedQuery<StudentCourseExemptionPaper> query = em.createQuery("SELECT e FROM StudentCourseExemptionPaper e WHERE e.status =:status", StudentCourseExemptionPaper.class);
        query.setParameter("status", VerificationStatus.PENDING);
        return query.getResultList();
    }

    public Set<StudentCourseExemptionPaper> findVerifiable(List<StudentCourseExemptionPaper> pending) {
        Set<StudentCourseExemptionPaper> verifiable = new HashSet<>();
        verifiable.addAll(pending);
        Set<StudentCourseExemptionPaper> toRemove = new HashSet<>();
        pending.stream().filter((e) -> (!e.getPaid() && e.getQualification().getType().equals("Kasneb"))).forEach((e) -> {
            toRemove.add(e);
        }); //If Kasneb and paid remove from collection
        verifiable.removeAll(toRemove);
        return verifiable;
    }

    public List<StudentCourseExemptionPaper> findPending(StudentCourse studentCourse) {
        TypedQuery<StudentCourseExemptionPaper> query = em.createQuery("SELECT e FROM StudentCourseExemptionPaper e WHERE e.studentCourse =:studentCourse", StudentCourseExemptionPaper.class);
        query.setParameter("studentCourse", studentCourse);
        return query.getResultList();
    }

    /**
     *
     * @param exemption
     * @throws CustomHttpException
     * @throws javax.mail.MessagingException
     */
    public void verify(Exemption exemption) throws CustomHttpException, MessagingException {
        Set<StudentCourseExemptionPaper> studentCourseExemptions = new HashSet<>();
        StudentCourse studentCourse = em.find(StudentCourse.class, exemption.getStudentCourseId());
        if (studentCourse == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student Course does not exist");
        }
        User verifier = em.find(User.class, exemption.getVerifiedBy());
        if (verifier == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Administrator does not exist");
        }
        if (exemption.getPapers() != null) {
            em.detach(studentCourse);
            List<StudentCourseExemptionPaper> pending = findPending(studentCourse);
            if (exemption.getPapers().containsAll(pending)) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Some papers have not been applied for exemption");
            }
            for (Paper paper : exemption.getPapers()) {
                //StudentCourseExemptionPaperPK studentCourseQualificationExemptionPK, StudentCourse studentCourse, Paper paper, StudentCourseQualification qualification, Boolean verified, VerificationStatus status
                StudentCourseExemptionPaperPK pk = new StudentCourseExemptionPaperPK(exemption.getStudentCourseId(), paper.getCode());
                //Get managed
                StudentCourseExemptionPaper managed = em.find(StudentCourseExemptionPaper.class, pk);
                if (managed == null) {
                    throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Some papers have not been applied for exemption");
                }
                em.detach(managed);
                managed.setDateVerified(new Date());
                managed.setVerifiedBy(verifier);
                managed.setStatus(exemption.getStatus());
                managed.setVerifyRemarks(exemption.getRemarks());
                managed.setVerified(Boolean.TRUE);
                studentCourseExemptions.add(managed);
                em.merge(managed);
            }
            studentCourse.setExemptions(studentCourseExemptions);
            //Generate exemption invoice
            ExemptionInvoice inv = invoiceFacade.generateExemptionInvoice(studentCourse);
            studentCourse.getInvoices().add(inv);
            String body;
//            //Send Email  
            if (studentCourse.getVerificationStatus() != VerificationStatus.REJECTED) {
                body = "Dear " + studentCourse.getStudentObj().getFirstName() + " " + studentCourse.getStudentObj().getMiddleName() + ",<br>\n"
                        + "Your application for exemption was successful. Kindly find the exemption letter attached.Wishing you the best in your studies.Your invoice number is " + inv.getReference() + " due Ksh " + inv.getKesTotal() + " generated on " + inv.getDateGenerated();
            } else {
                body = "Dear " + studentCourse.getStudentObj().getFirstName() + " " + studentCourse.getStudentObj().getMiddleName() + ",<br>\n"
                        + "Your application for exemption was unsuccessful. Kindly find the exemption letter attached.For further clarification, kindly contact KASNEB on Mobile: 0722201214, 0734600624, Tel: 020 2712640, 020 2712828,Email: info@kasneb.or.ke";
            }
            EmailUtil.sendEmail(new Email(studentCourse.getStudent().getLoginId().getEmail(), "Course registration verification", body));
            em.merge(studentCourse);
        }
    }

    public List<StudentCourseExemptionPaper> findSummary(Boolean verifiedStatus, Date startDate, Date endDate) {
        TypedQuery<StudentCourseExemptionPaper> query = em.createQuery("SELECT s FROM StudentCourseExemptionPaper s WHERE s.verified =:verified AND s.created BETWEEN :startDate AND :endDate", StudentCourseExemptionPaper.class);
        query.setParameter("verifiedStatus", verifiedStatus);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    public List<StudentCourseExemptionPaper> findSummary() {
        TypedQuery<StudentCourseExemptionPaper> query = em.createQuery("SELECT s FROM StudentCourseExemptionPaper s", StudentCourseExemptionPaper.class);
        return query.getResultList();
    }

}
