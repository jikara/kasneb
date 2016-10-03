/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.CentreRegion;
import com.kasneb.entity.CentreZone;
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
public class CentreRegionFacade extends AbstractFacade<CentreRegion> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CentreRegionFacade() {
        super(CentreRegion.class);
    }

    public Collection<CentreRegion> findByZone(Integer zoneId) throws CustomHttpException {
        CentreZone zone = em.find(CentreZone.class, zoneId);
        if (zone == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Zone does not exist");
        }
        TypedQuery<CentreRegion> query
                = em.createQuery("SELECT c FROM CentreRegion c WHERE c.zone =:zone", CentreRegion.class);
        query.setParameter("zone", zone);
        return query.getResultList();
    }

}
