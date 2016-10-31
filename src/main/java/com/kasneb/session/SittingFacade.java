/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.Sitting;
import com.kasneb.entity.SittingPeriod;
import com.kasneb.entity.Sitting_;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author jikara
 */
@Stateless
public class SittingFacade extends AbstractFacade<Sitting> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SittingFacade() {
        super(Sitting.class);
    }

    public List<Sitting> findEligible() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Sitting> cq = cb.createQuery(Sitting.class);
        Root<Sitting> sitting = cq.from(Sitting.class);
        cq.where(cb.greaterThanOrEqualTo(sitting.get(Sitting_.lateRegistrationDeadline), new Date()));
        TypedQuery<Sitting> query = em.createQuery(cq);
        return query.getResultList();
    }

    public Sitting find(SittingPeriod period, Integer year) {
        TypedQuery<Sitting> query = em.createQuery("SELECT s FROM Sitting s WHERE s.sittingPeriod =:period AND s.sittingYear =:year", Sitting.class);
        query.setParameter("period", period);
        query.setParameter("year", year);
        return query.getSingleResult();
    }

}
