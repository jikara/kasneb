/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.Course;
import com.kasneb.entity.StudentCourse;
import java.util.List;
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

    public List<Course> findByType(Integer code) {
        TypedQuery<Course> query
                = em.createNamedQuery("Course.findByCourseType", Course.class);
        query.setParameter("courseTypeCode", code);
        return query.getResultList();
    }

    public Course findByStudentCourse(StudentCourse studentCourse) {
//        TypedQuery<Course> query
//                = em.createNamedQuery("SELECT c FROM Course c WHERE c.", Course.class);
        //  query.setParameter("courseTypeCode", code);
        // return query.getSingleResult();
        return new Course(1);
    }

}
