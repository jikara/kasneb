/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.InvoiceStatus;
import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.StudentCourseSubscription;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author jikara
 */
@Stateless
public class StudentCourseSubscriptionFacade extends AbstractFacade<StudentCourseSubscription> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentCourseSubscriptionFacade() {
        super(StudentCourseSubscription.class);
    }

    public StudentCourseSubscription getLastSubscription1(StudentCourse currentCourse) {
        TypedQuery<StudentCourseSubscription> query = em.createQuery("SELECT s FROM StudentCourseSubscription s WHERE s.invoice.status =:status ORDER BY s.studentCourseSubscriptionPK.year DESC", StudentCourseSubscription.class);
        query.setMaxResults(1);
        query.setParameter("status", new InvoiceStatus("PAID"));
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ex) {
            return null;
        }
    }

    public StudentCourseSubscription getLastSubscription(StudentCourse currentCourse) {
        TypedQuery<StudentCourseSubscription> query = em.createQuery("SELECT s FROM StudentCourseSubscription s ORDER BY s.studentCourseSubscriptionPK.year DESC", StudentCourseSubscription.class);
        query.setMaxResults(1);
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ex) {
            return null;
        }
    }

}
