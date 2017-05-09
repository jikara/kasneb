/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.Fee;
import com.kasneb.entity.KasnebCourse;
import com.kasneb.entity.Level;
import com.kasneb.entity.Paper;
import com.kasneb.entity.Section;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.util.CoreUtil;
import java.io.IOException;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
             Logger.getLogger(FeeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        return fee;
    }

    public Fee getLateCourseRegistrationFee(KasnebCourse course) throws CustomHttpException {
        Fee fee = null;
        try {
            fee = CoreUtil.getLateRegistrationFee(course);
        } catch (IOException | CustomHttpException e) {
             Logger.getLogger(FeeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
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
             Logger.getLogger(FeeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
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
             Logger.getLogger(FeeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
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
             Logger.getLogger(FeeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
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
            Logger.getLogger(FeeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
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
            Logger.getLogger(FeeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        return fee;
    }

    public Fee getExemptionFee(Paper paper) throws CustomHttpException {
        Fee fee = null;
        try {
            fee = CoreUtil.getExemptionFee(paper, paper.getCourse());
        } catch (IOException | CustomHttpException e) {
            Logger.getLogger(FeeFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        return fee;
    }

}
