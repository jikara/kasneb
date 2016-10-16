/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.Institution;
import com.kasneb.entity.OtherCourse;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author jikara
 */
@Stateless
public class OtherCourseFacade extends AbstractFacade<OtherCourse> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OtherCourseFacade() {
        super(OtherCourse.class);
    }

    public Collection<OtherCourse> findByInstitution(Institution institution) {
        TypedQuery<OtherCourse> query
                = em.createQuery("SELECT c FROM OtherCourse c WHERE c.institution = :institution", OtherCourse.class);
        query.setParameter("institution", institution);
        return query.getResultList();
    }

}
