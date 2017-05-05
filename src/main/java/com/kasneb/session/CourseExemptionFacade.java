/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.dto.ExemptionCourse;
import com.kasneb.entity.Course;
import com.kasneb.entity.CourseExemption;
import com.kasneb.entity.KasnebCourse;
import com.kasneb.entity.Paper;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author jikara
 */
@Stateless
public class CourseExemptionFacade extends AbstractFacade<CourseExemption> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CourseExemptionFacade() {
        super(CourseExemption.class);
    }

    public Collection<CourseExemption> findByQualification1(Course qualification) {
        TypedQuery<CourseExemption> query
                = em.createQuery("SELECT c FROM CourseExemption c WHERE c.qualification = :qualification", CourseExemption.class);
        query.setParameter("qualification", qualification);
        for (CourseExemption exemption : query.getResultList()) {

        }

        return query.getResultList();
    }

    public com.kasneb.dto.QualificationExemption findByQualification(Course qualification) {
        TypedQuery<CourseExemption> query
                = em.createQuery("SELECT c FROM CourseExemption c WHERE c.qualification = :qualification", CourseExemption.class);
        query.setParameter("qualification", qualification);
        com.kasneb.dto.QualificationExemption myExemption = new com.kasneb.dto.QualificationExemption();
        List<CourseExemption> l = query.getResultList();
        for (CourseExemption exemption : l) {
            ExemptionCourse exemptionCourse = new ExemptionCourse(exemption.getCourse().getId(), exemption.getCourse().getName());
            myExemption.setId(exemption.getQualification().getId());
            myExemption.setName(exemption.getQualification().getName());
            myExemption.setInstitutionId(exemption.getQualification().getInstitution().getId());
            myExemption.setInstitutionName(exemption.getQualification().getInstitution().getName());
            exemptionCourse.setPapers(findExemptionPaper(exemption.getQualification(), exemption.getCourse()));
            myExemption.addExemptionCourse(exemptionCourse);
        }
        return myExemption;
    }

    public Set<Paper> findExemptionPaper(Course qualification, KasnebCourse course) {
        Set<Paper> papers = new HashSet<>();
        TypedQuery<CourseExemption> query
                = em.createQuery("SELECT c FROM CourseExemption c WHERE c.qualification = :qualification AND c.course = :course", CourseExemption.class);
        query.setParameter("qualification", qualification);
        query.setParameter("course", course);
        List<CourseExemption> l = query.getResultList();
        for (CourseExemption exemption : l) {
            papers.add(exemption.getPaper());
        }
        return papers;
    }

}
