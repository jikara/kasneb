/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.CentreCluster;
import com.kasneb.entity.Course;
import com.kasneb.entity.ExamCentre;
import com.kasneb.entity.NonKenyanCentre;
import com.kasneb.exception.CustomHttpException;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;

/**
 *
 * @author jikara
 */
@Stateless
public class ExamCentreFacade extends AbstractFacade<ExamCentre> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExamCentreFacade() {
        super(ExamCentre.class);
    }

    public Collection<ExamCentre> findByCluster(Integer clusterId) throws CustomHttpException {
        CentreCluster cluster = em.find(CentreCluster.class, clusterId);
        if (cluster == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Cluster does not exist");
        }
        TypedQuery<ExamCentre> query
                = em.createQuery("SELECT e FROM ExamCentre e WHERE e.clusterId = :clusterId", ExamCentre.class);
        query.setParameter("clusterId", cluster);
        return query.getResultList();
    }

    public Collection<ExamCentre> findByZone(Integer zoneId) throws CustomHttpException {
        NonKenyanCentre zone = em.find(NonKenyanCentre.class, zoneId);
        if (zone == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Zone does not exist");
        }
        TypedQuery<ExamCentre> query
                = em.createQuery("SELECT e FROM ExamCentre e WHERE e.zone = :zone", ExamCentre.class);
        query.setParameter("zone", zone);
        return query.getResultList();
    }

    public Collection<ExamCentre> findByCourseAndCluster(Integer courseId, Integer clusterId) throws CustomHttpException {
        CentreCluster cluster = em.find(CentreCluster.class, clusterId);
        Course course = em.find(Course.class, courseId);
        if (cluster == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Cluster does not exist");
        }
        if (course == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Course does not exist");
        }
        TypedQuery<ExamCentre> query
                = em.createQuery("SELECT e FROM ExamCentre e JOIN e.examsOffered c WHERE e.clusterId = :clusterId AND c=:courseId", ExamCentre.class);
        query.setParameter("clusterId", cluster);
        query.setParameter("courseId", course);
        return query.getResultList();
    }

    public Collection<ExamCentre> findByCourseAndZone(Integer courseId, Integer zoneId) throws CustomHttpException {
        NonKenyanCentre zone = em.find(NonKenyanCentre.class, zoneId);
        Course course = em.find(Course.class, courseId);
        if (zone == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Zone does not exist");
        }
        if (course == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Course does not exist");
        }
        TypedQuery<ExamCentre> query
                = em.createQuery("SELECT e FROM ExamCentre e JOIN e.examsOffered c  WHERE e.zone = :zone AND c=:courseId", ExamCentre.class);
        query.setParameter("zone", zone);
        query.setParameter("courseId", course);
        return query.getResultList();
    }

    public Collection<ExamCentre> findByCourse(Integer courseId) throws CustomHttpException {
        Course course = em.find(Course.class, courseId);
        if (course == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Course does not exist");
        }
        TypedQuery<ExamCentre> query
                = em.createQuery("SELECT e FROM ExamCentre e JOIN e.examsOffered c WHERE c=:course", ExamCentre.class);
        query.setParameter("course", course);
        return query.getResultList();
    }

    public Collection<ExamCentre> findByRegion(Integer regionId) throws CustomHttpException {
        Course course = em.find(Course.class, regionId);
        if (course == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Course does not exist");
        }
        TypedQuery<ExamCentre> query
                = em.createQuery("SELECT e FROM ExamCentre e JOIN e.examsOffered c WHERE c=:course", ExamCentre.class);
        query.setParameter("course", course);
        return query.getResultList();
    }

    public Collection<ExamCentre> findByCourseAndRegion(Integer courseId, Integer regionId) throws CustomHttpException {
        Course course = em.find(Course.class, courseId);
        if (course == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Course does not exist");
        }
        TypedQuery<ExamCentre> query
                = em.createQuery("SELECT e FROM ExamCentre e JOIN e.examsOffered c WHERE c=:course", ExamCentre.class);
        query.setParameter("course", course);
        return query.getResultList();
    }

}
