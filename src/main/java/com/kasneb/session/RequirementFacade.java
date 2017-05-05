/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.KasnebCourseType;
import com.kasneb.entity.Requirement;
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
public class RequirementFacade extends AbstractFacade<Requirement> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RequirementFacade() {
        super(Requirement.class);
    }

    public List<Requirement> findByCourseType(KasnebCourseType courseType) {
        TypedQuery<Requirement> query = em.createQuery("SELECT r FROM Requirement r WHERE r.courseType =:courseType", Requirement.class);
        query.setParameter("courseType", courseType);
        return query.getResultList();
    }

}
