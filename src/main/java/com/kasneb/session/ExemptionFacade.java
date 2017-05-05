/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kasneb.client.CoreExemption;
import com.kasneb.entity.Exemption;
import com.kasneb.entity.ExemptionInvoiceDetail;
import com.kasneb.entity.ExemptionPaper;
import com.kasneb.entity.ExemptionStatus;
import com.kasneb.entity.Invoice;
import com.kasneb.entity.Paper;
import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.StudentCoursePaper;
import com.kasneb.entity.Synchronization;
import com.kasneb.entity.User;
import com.kasneb.entity.VerificationStatus;
import com.kasneb.entity.pk.StudentCoursePaperPK;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.util.CoreUtil;
import com.kasneb.util.DateUtil;
import com.kasneb.util.GeneratorUtil;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author jikara
 */
@Stateless
public class ExemptionFacade extends AbstractFacade<Exemption> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    @EJB
    com.kasneb.session.InvoiceFacade invoiceFacade;
    @EJB
    com.kasneb.session.StudentCourseFacade studentCourseFacade;
    @EJB
    com.kasneb.session.CommunicationFacade communicationFacade;
    @EJB
    com.kasneb.session.ExemptionPaperFacade exemptionPaperFacade;
    @EJB
    com.kasneb.session.PaymentFacade paymentFacade;
    @EJB
    com.kasneb.session.StudentFacade studentFacade;
    @EJB
    com.kasneb.session.SynchronizationFacade synchronizationFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExemptionFacade() {
        super(Exemption.class);
    }

    @Override
    public List<Exemption> findAll() {
        TypedQuery<Exemption> query = em.createQuery("SELECT e FROM Exemption e ORDER BY e.dateVerified DESC", Exemption.class);
        query.setMaxResults(500);
        return query.getResultList();
    }

    public List<Exemption> findAll(Integer userId, Date startDate, Date endDate) {
        TypedQuery<Exemption> query = em.createQuery("SELECT e FROM Exemption e WHERE e.status =:status ORDER BY e.dateVerified DESC", Exemption.class);
        query.setParameter("status", ExemptionStatus.PENDING);
        query.setMaxResults(500);
        return query.getResultList();
    }

    public List<Exemption> findPending() {
        TypedQuery<Exemption> query = em.createQuery("SELECT e FROM Exemption e WHERE e.status =:status ORDER BY e.dateVerified DESC", Exemption.class);
        query.setParameter("status", ExemptionStatus.PENDING);
        return query.getResultList();
    }

    public List<Exemption> findSummary(Boolean verified, Date startDate, Date endDate) {
        TypedQuery<Exemption> query = em.createQuery("SELECT e FROM Exemption e WHERE e.verified =:verified AND e.created BETWEEN :startDate AND :endDate ORDER BY e.dateVerified DESC", Exemption.class);
        query.setParameter("verifiedStatus", verified);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    public List<Exemption> findSummary(Boolean verified, Date startDate, Date endDate, Integer userId) {
        TypedQuery<Exemption> query = em.createQuery("SELECT e FROM Exemption e WHERE e.verified =:verified AND e.verifiedBy =:user AND e.created BETWEEN :startDate AND :endDate ORDER BY e.dateVerified DESC", Exemption.class);
        query.setParameter("verified", verified);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("user", new User(userId));
        query.setMaxResults(500);
        return query.getResultList();
    }

    public List<Exemption> findSummary(Integer userId) {
        TypedQuery<Exemption> query = em.createQuery("SELECT e FROM Exemption e WHERE e.verifiedBy =:user ORDER BY e.dateVerified DESC", Exemption.class);
        query.setParameter("user", new User(userId));
        query.setMaxResults(500);
        return query.getResultList();
    }

    public List<Exemption> findSummary(Date startDate, Date endDate) {
        TypedQuery<Exemption> query = em.createQuery("SELECT e FROM Exemption e WHERE e.created BETWEEN :startDate AND :endDate ORDER BY e.dateVerified DESC", Exemption.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    public List<Exemption> findSummary(Date startDate, Date endDate, Integer userId) {
        TypedQuery<Exemption> query = em.createQuery("SELECT e FROM Exemption e WHERE e.verifiedBy =:user AND e.created BETWEEN :startDate AND :endDate ORDER BY e.dateVerified DESC", Exemption.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("user", new User(userId));
        return query.getResultList();
    }

    public Exemption verify(Exemption entity) throws CustomHttpException, IllegalAccessException, InvocationTargetException {
        boolean all = verifyAll(entity);
        Exemption managed = super.find(entity.getId());
        if (all) {
            invoiceFacade.generateExemptionInvoice(managed);
        }
        return managed;
    }

    public boolean verifyAll(Exemption entity) throws IllegalAccessException, InvocationTargetException {
        for (ExemptionPaper exemptionPaper : entity.getPapers()) {
            exemptionPaper.setVerified(Boolean.TRUE);
            exemptionPaper.setDateVerified(new Date());
        }
        Exemption managed = super.find(entity.getId());
        List<ExemptionPaper> pendingPapers = managed.getPendingPapers();
        int paperCount = entity.getPapers().size();
        super.copy(entity, managed);
        boolean all = pendingPapers.size() == paperCount;
        if (all) { //All papers verified            
            managed.setVerified(Boolean.TRUE);
            managed.setStatus(ExemptionStatus.COMPLETED);
            managed.setDateVerified(new Date());
        }
        super.edit(managed);
        return all;
    }

    public Exemption createExemption(Exemption exemption) throws CustomHttpException {
        
        
        
        StudentCourse managed = studentCourseFacade.find(exemption.getStudentCourse().getId());
        if (exemption.getQualifications() != null && exemption.getQualifications().size() > 0) {  //Combined.
            exemption.setStatus(ExemptionStatus.PENDING);
            exemption.setVerified(Boolean.FALSE);
            for (ExemptionPaper exemptionPaper : exemption.getPapers()) {
                exemptionPaper.setCreated(new Date());
                exemptionPaper.setVerified(Boolean.FALSE);
            }
        } else {
            if (exemption.getQualifications() != null && (exemption.getQualifications().get(0) == null || !exemption.getQualifications().get(0).getId().equals("1000"))) {
                exemption.setDateVerified(new Date());
                exemption.setStatus(ExemptionStatus.PENDING);
                exemption.setVerified(Boolean.TRUE);
                for (ExemptionPaper exemptionPaper : exemption.getPapers()) {
                    exemptionPaper.setVerified(Boolean.TRUE);
                    exemptionPaper.setVerificationStatus(VerificationStatus.APPROVED);
                }
            } else {
                exemption.setStatus(ExemptionStatus.PENDING);
                exemption.setVerified(Boolean.FALSE);
                for (ExemptionPaper exemptionPaper : exemption.getPapers()) {
                    exemptionPaper.setCreated(new Date());
                    exemptionPaper.setVerified(Boolean.FALSE);
                }
            }
        }
        exemption.setCreated(new Date());
        exemption.setReference(DateUtil.getYear(new Date()) + "/" + managed.getCourse().getName() + "/" + GeneratorUtil.generateInvoiceNumber());
        super.create(exemption);
        return exemption;
        
        
        
        
    }

    public void updatePaidPapers(Invoice invoice) {
        for (ExemptionInvoiceDetail invDetail : invoice.getExemptionInvoiceDetails()) {
            //Get managed 
            ExemptionPaper managed = em.find(ExemptionPaper.class, invDetail.getPaper().getExemptionPaperPK());
            managed.setPaid(Boolean.TRUE);
            em.merge(managed);
        }
    }

    public Exemption findPending(Integer id) {
        TypedQuery<Exemption> query = em.createQuery("SELECT e FROM Exemption e WHERE e.id =:id", Exemption.class);
        query.setParameter("id", id);
        query.setMaxResults(1);
        try {
            Exemption e = query.getSingleResult();
            List<ExemptionPaper> papers = exemptionPaperFacade.findPending(e.getId());
            e.setPapers(papers);
            return e;
        } catch (NoResultException ex) {
            return null;
        }
    }

    public void createCoreExemption(Exemption exemption) throws IOException, JsonProcessingException, CustomHttpException, ParseException {
        Exemption managed = em.find(Exemption.class, exemption.getId());
        List<CoreExemption> exemptions = new ArrayList<>();
        for (ExemptionPaper exemptionPaper : exemption.getPapers()) {
            Paper paper = exemptionPaper.getPaper();
            CoreExemption coreExemption = new CoreExemption(managed.getStudentCourse().getStream().toString(), "X", exemption.getReference());
            CoreExemption.ExemptionPK pk = null;
            switch (managed.getStudentCourse().getCourse().getCourseTypeCode()) {
                case 100:
                    pk = coreExemption.new ExemptionPK(managed.getStudentCourse().getRegistrationNumber(), paper.getSection().getSectionPK().getId(), null, paper.getCode());
                    break;
                case 200:
                    pk = coreExemption.new ExemptionPK(managed.getStudentCourse().getRegistrationNumber(), null, paper.getLevel().getLevelPK().getId(), paper.getCode());
                    break;
            }
            coreExemption.setPk(pk);
            exemptions.add(coreExemption);
        }
        //Create receipt
        paymentFacade.createReceipt(managed.getInvoice());
        //Create Exemption
        CoreUtil.createExemption(exemptions, managed.getStudentCourse().getCourse());
        //Remove exempted papers
        Synchronization synchronization = new Synchronization(managed.getStudentCourse().getStudent(), false);
        synchronizationFacade.create(synchronization);
    }

    public void removeExemptedPapers(Exemption exemption) {
        for (ExemptionPaper exemptionPaper : exemption.getPapers()) {
            StudentCoursePaper managed = em.find(StudentCoursePaper.class, new StudentCoursePaperPK(exemption.getStudentCourse().getId(), exemptionPaper.getPaper().getCode()));
            if (managed != null) {
                em.remove(managed);
            }
        }
    }

    public List<com.kasneb.dto.Exemption> findByStudentCourse(StudentCourse studentCourse) {
        TypedQuery<com.kasneb.dto.Exemption> query = em.createQuery("SELECT new com.kasneb.dto.Exemption(e.id,e.reference,e.dateVerified)  FROM Exemption e WHERE e.studentCourse =:studentCourse AND e.status =:status", com.kasneb.dto.Exemption.class);
        query.setParameter("studentCourse", studentCourse);
        query.setParameter("status", ExemptionStatus.COMPLETED);
        return query.getResultList();
    }

}
