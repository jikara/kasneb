/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.FeeCode;
import com.kasneb.entity.Fee;
import com.kasneb.entity.FeeTypeCode;
import com.kasneb.entity.Fee_;
import com.kasneb.entity.KasnebCourse;
import com.kasneb.entity.Level;
import com.kasneb.entity.Paper;
import com.kasneb.entity.Part;
import com.kasneb.entity.Section;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.util.CoreUtil;
import java.io.IOException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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
public class FeeFacade extends AbstractFacade<Fee> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FeeFacade() {
        super(Fee.class);
    }

    public Fee getCourseRegistrationFee(KasnebCourse course) throws CustomHttpException, IOException {
        Fee fee = null;
        try {
            fee = CoreUtil.getRegistrationFee(course);
        } catch (IOException | CustomHttpException e) {
           // Logger.getLogger(FeeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        return fee;
    }

    public Fee getLateCourseRegistrationFee(KasnebCourse course) throws CustomHttpException {
        Fee fee = null;
        try {
            fee = CoreUtil.getLateRegistrationFee(course);
        } catch (IOException | CustomHttpException e) {
           // Logger.getLogger(FeeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        return fee;
    }

    public Fee getAnnualRegistrationRenewalFee(KasnebCourse course, Integer year) throws CustomHttpException {
        Fee fee = null;
        try {
            if (course == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Course does not exist");
            }
            fee = CoreUtil.getRenewalFee(course, year);
        } catch (IOException | CustomHttpException e) {
           // Logger.getLogger(FeeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        return fee;
    }

    public Fee getRegistrationReactivationFee(KasnebCourse course) throws CustomHttpException {
        Fee fee = null;
        try {
            if (course == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Course does not exist");
            }
            fee = CoreUtil.getReinstatementFee(course);
        } catch (IOException | CustomHttpException e) {
           // Logger.getLogger(FeeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        return fee;
    }

    public Fee getStudentCardReplacementFee(KasnebCourse course) throws CustomHttpException {
        Fee fee = null;
        if (course == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Course does not exist");
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> ft = cq.from(Fee.class);

        cq.where(cb.equal(ft.get(Fee_.courseType), course.getKasnebCourseType()),
                cb.and(cb.equal(ft.get(Fee_.feeCode), new FeeCode("REGISTRATION_FEE"))),
                cb.and(cb.equal(ft.get(Fee_.feeTypeCode), new FeeTypeCode("student_identity_card_replacement_fees"))));
        TypedQuery<Fee> query = em.createQuery(cq);
        query.setMaxResults(1);
        try {
            fee = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "No id card replacement fee configured for this course");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than one id card replacement fee configured for this course");
        }
        return fee;
    }

    public Fee getExamEntryFeePerLevel(Level level, Boolean late) throws CustomHttpException {
        Fee fee = null;
        try {
            if (late) {
                fee = CoreUtil.getLevelLateExaminationFee(level, level.getCourse());
            } else {
                fee = CoreUtil.getLevelExaminationFee(level, level.getCourse());
            }
        } catch (IOException | CustomHttpException e) {
           // Logger.getLogger(FeeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        return fee;
    }

    public Fee getExamEntryFeePerSection(Section section, Boolean late) throws CustomHttpException {
        Fee fee = null;
        try {
            if (late) {
                fee = CoreUtil.getSectionLateExaminationFee(section, section.getPart().getCourse());
            } else {
                fee = CoreUtil.getSectionExaminationFee(section, section.getPart().getCourse());
            }
        } catch (IOException | CustomHttpException e) {
           // Logger.getLogger(FeeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        return fee;
    }

    public Fee getExamEntryFeePerPaper(Paper paper, Boolean late) throws CustomHttpException {
        Fee fee = null;
        try {
            if (late) {
                fee = CoreUtil.getPaperLateExaminationFee(paper, paper.getCourse());
            } else {
                fee = CoreUtil.getPaperExaminationFee(paper, paper.getCourse());
            }
        } catch (IOException | CustomHttpException e) {
           // Logger.getLogger(FeeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        return fee;
    }

    public Fee getExemptionFee(Paper paper) throws CustomHttpException {
        Fee fee = null;
        try {
            fee = CoreUtil.getExemptionFee(paper, paper.getCourse());
        } catch (IOException | CustomHttpException e) {
           // Logger.getLogger(FeeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        return fee;
    }

    public Fee getExemptionFee(Part part) throws CustomHttpException {
        Fee fee = null;
        part = em.find(Part.class, part.getPartPK().getId());
        if (part == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Part does not exist");
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> ft = cq.from(Fee.class);

        cq.where(cb.equal(ft.get(Fee_.part), part),
                cb.and(cb.equal(ft.get(Fee_.feeCode), new FeeCode("EXEMPTION_FEE"))),
                cb.and(cb.equal(ft.get(Fee_.feeTypeCode), new FeeTypeCode("exemptions_per_part"))));
        TypedQuery<Fee> query = em.createQuery(cq);
        try {
            fee = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This fee is not configured");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than one exam entry fee is configured for this paper");
        }
        return fee;
    }

    public Fee getSyllabusPulicationFee() throws CustomHttpException {
        Fee fee = null;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> ft = cq.from(Fee.class);

        cq.where(cb.equal(ft.get(Fee_.feeCode), new FeeCode("PUBLICATION_FEE")),
                cb.and(cb.equal(ft.get(Fee_.feeTypeCode), new FeeTypeCode("sale_of_syllabus"))));
        TypedQuery<Fee> query = em.createQuery(cq);
        try {
            fee = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This fee  is not configured");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than one sale of syllabus is configured");
        }
        return fee;
    }

    public Fee getPastPaperPublicationFee() throws CustomHttpException {
        Fee fee = null;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> ft = cq.from(Fee.class);

        cq.where(cb.equal(ft.get(Fee_.feeCode), new FeeCode("PUBLICATION_FEE")),
                cb.and(cb.equal(ft.get(Fee_.feeTypeCode), new FeeTypeCode("sale_of_past_papers"))));
        TypedQuery<Fee> query = em.createQuery(cq);
        try {
            fee = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This fee  is not configured");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than  sale of past papers fee is configured");
        }
        return fee;
    }

    public Fee getAdministrativeFee() throws CustomHttpException {
        Fee fee = null;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> ft = cq.from(Fee.class);
        cq.where(cb.equal(ft.get(Fee_.feeCode), new FeeCode("ADMINISTRATIVE_FEE")),
                cb.and(cb.equal(ft.get(Fee_.feeTypeCode), new FeeTypeCode("administrative_fee"))));
        TypedQuery<Fee> query = em.createQuery(cq);
        try {
            fee = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This fee  is not configured");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than  sale of past papers fee is configured");
        }
        return fee;
    }

}
