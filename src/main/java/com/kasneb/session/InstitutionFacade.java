/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.Institution;
import com.kasneb.entity.OtherCourseType;
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
public class InstitutionFacade extends AbstractFacade<Institution> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InstitutionFacade() {
        super(Institution.class);
    }

    public Collection<Institution> findByCourseType(OtherCourseType courseType) {
        TypedQuery<Institution> query
                = em.createQuery("SELECT i FROM Institution i WHERE i.courseType = :courseType", Institution.class);
        query.setParameter("courseType", courseType);
        return query.getResultList();
    }

}
