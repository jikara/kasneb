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

    public Fee getCourseRegistrationFeeType(KasnebCourse course) throws CustomHttpException {
        Fee feeType = null;
        if (course == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Course does not exist");
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> ft = cq.from(Fee.class);

        cq.where(cb.equal(ft.get(Fee_.courseType), course.getKasnebCourseType()),
                cb.and(cb.equal(ft.get(Fee_.feeCode), new FeeCode("REGISTRATION_FEE"))),
                cb.and(cb.equal(ft.get(Fee_.feeTypeCode), new FeeTypeCode("course_registration_fees"))));
        TypedQuery<Fee> query = em.createQuery(cq);
        try {
            feeType = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "No registration fee configured for this course");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than one registration fee configured for this course");
        }
        return feeType;
    }

    public Fee getLateCourseRegistrationFeeType(KasnebCourse course) throws CustomHttpException {
        Fee feeType = null;
        if (course == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Course does not exist");
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> ft = cq.from(Fee.class);
        cq.where(cb.equal(ft.get(Fee_.courseType), course.getKasnebCourseType()),
                cb.and(cb.equal(ft.get(Fee_.feeCode), new FeeCode("REGISTRATION_FEE"))),
                cb.and(cb.equal(ft.get(Fee_.feeTypeCode), new FeeTypeCode("course_registration_fees"))));
        TypedQuery<Fee> query = em.createQuery(cq);
        try {
            feeType = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "No registration fee configured for this course");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than one registration fee configured for this course");
        }
        return feeType;
    }

    public Fee getAnnualRegistrationRenewalFee(KasnebCourse course) throws CustomHttpException {
        Fee feeType = null;
        if (course == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Course does not exist");
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> ft = cq.from(Fee.class);

        cq.where(cb.equal(ft.get(Fee_.courseType), course.getKasnebCourseType()),
                cb.and(cb.equal(ft.get(Fee_.feeCode), new FeeCode("REGISTRATION_RENEWAL_FEE"))),
                cb.and(cb.equal(ft.get(Fee_.feeTypeCode), new FeeTypeCode("annual_registration_renewal_fees"))));
        TypedQuery<Fee> query = em.createQuery(cq);
        try {
            feeType = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "No renewal fee configured for this course");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than one renewal fee configured for this course");
        }
        return feeType;
    }

    public Fee getRegistrationReactivationFee(KasnebCourse course) throws CustomHttpException {
        Fee feeType = null;
        if (course == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Course does not exist");
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> ft = cq.from(Fee.class);

        cq.where(cb.equal(ft.get(Fee_.courseType), course.getKasnebCourseType()),
                cb.and(cb.equal(ft.get(Fee_.feeCode), new FeeCode("REGISTRATION_FEE"))),
                cb.and(cb.equal(ft.get(Fee_.feeTypeCode), new FeeTypeCode("registration_reactivation_fees"))));
        TypedQuery<Fee> query = em.createQuery(cq);
        try {
            feeType = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "No reactivation fee configured for this course");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than one reactivation fee configured for this course");
        }
        return feeType;
    }

    public Fee getStudentCardReplacementFee(KasnebCourse course) throws CustomHttpException {
        Fee feeType = null;
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
        try {
            feeType = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "No id card replacement fee configured for this course");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than one id card replacement fee configured for this course");
        }
        return feeType;
    }

    public Fee getExamEntryFeePerPaper(Paper paper) throws CustomHttpException {
        Fee feeType = null;
        if (paper == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Paper does not exist");
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> ft = cq.from(Fee.class);

        cq.where(cb.equal(ft.get(Fee_.paper), paper),
                cb.and(cb.equal(ft.get(Fee_.feeCode), new FeeCode("EXAM_ENTRY_FEE"))),
                cb.and(cb.equal(ft.get(Fee_.feeTypeCode), new FeeTypeCode("exam_entry_fee_per_paper"))));
        TypedQuery<Fee> query = em.createQuery(cq);
        try {
            feeType = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This fee  is not configured");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than one exam entry fee is configured for this paper");
        }
        return feeType;
    }

    public Fee getExamEntryFeePerLevel(Level level) throws CustomHttpException {
        Fee feeType = null;
        level = em.find(Level.class, level.getId());
        if (level == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Level does not exist");
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> ft = cq.from(Fee.class);

        cq.where(cb.equal(ft.get(Fee_.level), level),
                cb.and(cb.equal(ft.get(Fee_.feeCode), new FeeCode("EXAM_ENTRY_FEE"))),
                cb.and(cb.equal(ft.get(Fee_.feeTypeCode), new FeeTypeCode("exam_entry_fee_per_paper"))));
        TypedQuery<Fee> query = em.createQuery(cq);
        try {
            feeType = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This fee  is not configured");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than one exam entry fee is configured for this paper");
        }
        return feeType;
    }

    public Fee getExamEntryFeePerPart(Part part) throws CustomHttpException {
        Fee feeType = null;
        part = em.find(Part.class, part.getId());
        if (part == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Part does not exist");
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> ft = cq.from(Fee.class);

        cq.where(cb.equal(ft.get(Fee_.part), part),
                cb.and(cb.equal(ft.get(Fee_.feeCode), new FeeCode("EXAM_ENTRY_FEE"))),
                cb.and(cb.equal(ft.get(Fee_.feeTypeCode), new FeeTypeCode("exam_entry_fee_per_part"))));
        TypedQuery<Fee> query = em.createQuery(cq);
        try {
            feeType = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This fee  is not configured");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than one exam entry fee is configured for this part");
        }
        return feeType;
    }

    public Fee getExamEntryFeePerSection(Section section) throws CustomHttpException {
        Fee feeType = null;
        section = em.find(Section.class, section.getId());
        if (section == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Section does not exist");
        }
        TypedQuery<Fee> query = em.createQuery("SELECT f FROM Fee f WHERE f.id=9", Fee.class);
        //  query.setParameter("section", section);
        try {
            feeType = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This fee is not configured");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than one exam entry fee is configured for this section");
        }
        return feeType;
    }

    public Fee getExamEntryFeePerSection1(Section section) throws CustomHttpException {
        Fee feeType = null;
        section = em.find(Section.class, section.getId());
        if (section == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Section does not exist");
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> ft = cq.from(Fee.class);

        cq.where(cb.equal(ft.get(Fee_.section), section),
                cb.and(cb.equal(ft.get(Fee_.feeCode), new FeeCode("EXAM_ENTRY_FEE"))),
                cb.and(cb.equal(ft.get(Fee_.feeTypeCode), new FeeTypeCode("exam_entry_fee_per_section"))));
        TypedQuery<Fee> query = em.createQuery(cq);
        try {
            feeType = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This fee is not configured");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than one exam entry fee is configured for this section");
        }
        return feeType;
    }

    public Fee getExemptionFee(Paper paper) throws CustomHttpException {
        Fee feeType = null;
        paper = em.find(Paper.class, paper.getCode());
        if (paper == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Paper does not exist");
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> ft = cq.from(Fee.class);

        cq.where(cb.equal(ft.get(Fee_.paper), paper),
                cb.and(cb.equal(ft.get(Fee_.feeCode), new FeeCode("EXAM_ENTRY_FEE"))),
                cb.and(cb.equal(ft.get(Fee_.feeTypeCode), new FeeTypeCode("exam_entry_fee_per_paper"))));
        TypedQuery<Fee> query = em.createQuery(cq);
        try {
            feeType = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This fee is not configured");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than one exam entry fee is configured for this paper");
        }
        return feeType;
    }

    public Fee getExemptionFee(Part part) throws CustomHttpException {
        Fee feeType = null;
        part = em.find(Part.class, part.getId());
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
            feeType = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This fee is not configured");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than one exam entry fee is configured for this paper");
        }
        return feeType;
    }

    public Fee getSyllabusPulicationFee() throws CustomHttpException {
        Fee feeType = null;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> ft = cq.from(Fee.class);

        cq.where(cb.equal(ft.get(Fee_.feeCode), new FeeCode("PUBLICATION_FEE")),
                cb.and(cb.equal(ft.get(Fee_.feeTypeCode), new FeeTypeCode("sale_of_syllabus"))));
        TypedQuery<Fee> query = em.createQuery(cq);
        try {
            feeType = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This fee  is not configured");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than one sale of syllabus is configured");
        }
        return feeType;
    }

    public Fee getPastPaperPublicationFee() throws CustomHttpException {
        Fee feeType = null;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> ft = cq.from(Fee.class);

        cq.where(cb.equal(ft.get(Fee_.feeCode), new FeeCode("PUBLICATION_FEE")),
                cb.and(cb.equal(ft.get(Fee_.feeTypeCode), new FeeTypeCode("sale_of_past_papers"))));
        TypedQuery<Fee> query = em.createQuery(cq);
        try {
            feeType = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This fee  is not configured");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than  sale of past papers fee is configured");
        }
        return feeType;
    }

    public Fee getAdministrativeFee() throws CustomHttpException {
        Fee feeType = null;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fee> cq = cb.createQuery(Fee.class);
        Root<Fee> ft = cq.from(Fee.class);
        cq.where(cb.equal(ft.get(Fee_.feeCode), new FeeCode("ADMINISTRATIVE_FEE")));
        TypedQuery<Fee> query = em.createQuery(cq);
        try {
            feeType = query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This fee  is not configured");
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "More than one administrative fee is configured");
        }
        return feeType;
    }

}
