/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.Course;
import com.kasneb.entity.CourseExemption;
import com.kasneb.util.GeneratorUtil;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author jikara
 */
@Stateless
public class CourseFacade extends AbstractFacade<Course> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CourseFacade() {
        super(Course.class);
    }

    public String getMaximun() {
        return GeneratorUtil.generateRandomPin();
    }

    public CourseExemption findCourseExemption(String qualificationId, String paperCode) {
        TypedQuery<CourseExemption> query = em.createQuery("SELECT c FROM CourseExemption c WHERE c.qualification.id =:qualificationId AND c.paper.code =:paperCode", CourseExemption.class);
        query.setParameter("qualificationId", qualificationId);
        query.setParameter("paperCode", paperCode);
        query.setMaxResults(1);
        return query.getSingleResult();
    }

}
