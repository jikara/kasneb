/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.Sitting;
import com.kasneb.entity.SittingPeriod;
import java.util.Date;
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
        TypedQuery<Sitting> query = em.createQuery("SELECT s FROM Sitting s WHERE s.lateRegistrationDeadline >= :deadline", Sitting.class);
        query.setParameter("deadline", new Date());
        return query.getResultList();
    }

    public Sitting find(SittingPeriod period, Integer year) {
        TypedQuery<Sitting> query = em.createQuery("SELECT s FROM Sitting s WHERE s.sittingPeriod =:period AND s.sittingYear =:year", Sitting.class);
        query.setParameter("period", period);
        query.setParameter("year", year);
        query.setMaxResults(1);
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ex) {
            return null;
        }
    }

    public Sitting findSitting(Integer partYear, Integer year) {
        SittingPeriod period = null;
        switch (partYear) {
            case 1:
                period = SittingPeriod.MAY;
                break;
            case 2:
                period = SittingPeriod.NOVEMBER;
                break;
        }
        TypedQuery<Sitting> query = em.createQuery("SELECT s FROM Sitting s WHERE s.sittingPeriod =:period AND s.sittingYear =:year", Sitting.class);
        query.setParameter("period", period);
        query.setParameter("year", year);
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ex) {
            Sitting entity = new Sitting(partYear, year);
            em.persist(entity);
            return entity;
        }
    }

}
