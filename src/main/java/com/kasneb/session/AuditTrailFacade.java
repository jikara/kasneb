/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.AuditTrail;
import com.kasneb.entity.User;
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
public class AuditTrailFacade extends AbstractFacade<AuditTrail> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AuditTrailFacade() {
        super(AuditTrail.class);
    }

    public Collection<AuditTrail> findByUser(Integer userId) throws CustomHttpException {
        User user = em.find(User.class, userId);
        if (user == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "User does not exist");
        }
        TypedQuery<AuditTrail> query = em.createQuery("SELECT a FROM AuditTrail a WHERE a.user =:user", AuditTrail.class);
        query.setParameter("user", user);
        return query.getResultList();
    }

}
