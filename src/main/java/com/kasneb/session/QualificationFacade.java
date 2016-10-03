/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.Qualification;
import com.kasneb.entity.QualificationType;
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
public class QualificationFacade extends AbstractFacade<Qualification> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public QualificationFacade() {
        super(Qualification.class);
    }

    public Collection<Qualification> findByType(QualificationType type) {
        TypedQuery<Qualification> query
                = em.createQuery("SELECT q FROM Qualification q WHERE q.type=:type", Qualification.class);
        query.setParameter("type", type);
        return query.getResultList();
    }

}
