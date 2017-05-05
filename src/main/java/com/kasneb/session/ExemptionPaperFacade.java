/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.ExemptionPaper;
import com.kasneb.entity.VerificationStatus;
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
public class ExemptionPaperFacade extends AbstractFacade<ExemptionPaper> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExemptionPaperFacade() {
        super(ExemptionPaper.class);
    }

    public List<ExemptionPaper> findPending(Integer id) {
        TypedQuery<ExemptionPaper> query = em.createQuery("SELECT e FROM ExemptionPaper e WHERE e.exemptionPaperPK.exemptionId =:id AND  e.verificationStatus =:verificationStatus", ExemptionPaper.class);
        query.setParameter("id", id);
        query.setParameter("verificationStatus", VerificationStatus.PENDING);
        return query.getResultList();
    }

}
