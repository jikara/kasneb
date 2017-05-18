/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.Part;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author jikara
 */
@Stateless
public class PartFacade extends AbstractFacade<Part> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PartFacade() {
        super(Part.class);
    }

    @Override
    public Part find(Object pk) {
        try {
            TypedQuery<Part> query = em.createQuery("select p from Part p left join fetch p.sectionCollection WHERE p.partPK =:pk", Part.class);
            query.setParameter("pk", pk);
            Part part = query.getSingleResult();
            return part;
        } catch (javax.persistence.NoResultException ex) {
            return null;
        }
    }

}
