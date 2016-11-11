/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.ExamCentre;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.util.CoreUtil;
import java.io.IOException;
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
public class ExamCentreFacade extends AbstractFacade<ExamCentre> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExamCentreFacade() {
        super(ExamCentre.class);
    }

    public List<ExamCentre> findByZone(String zoneCode) throws CustomHttpException, IOException {
        return CoreUtil.getCentres(zoneCode);
    }

    public List<ExamCentre> findCentres() throws CustomHttpException, IOException {
        List<ExamCentre> centres = CoreUtil.getCentres();
        for (ExamCentre c : centres) {
            em.merge(c);
        }
        TypedQuery<ExamCentre> query
                = em.createQuery("SELECT e FROM ExamCentre e", ExamCentre.class);
        return query.getResultList();
    }

}
