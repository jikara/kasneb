/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.CentreCluster;
import com.kasneb.entity.CentreRegion;
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
public class CentreClusterFacade extends AbstractFacade<CentreCluster> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CentreClusterFacade() {
        super(CentreCluster.class);
    }

    public Collection<CentreCluster> findByRegion(Integer regionId) throws CustomHttpException {
        CentreRegion region = em.find(CentreRegion.class, regionId);
        if (region == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Region does not exist");
        }
        TypedQuery<CentreCluster> query
                = em.createQuery("SELECT c FROM CentreCluster c WHERE c.region = :region", CentreCluster.class);
        query.setParameter("region", region);
        return query.getResultList();
    }

}
