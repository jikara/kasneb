/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.CentreZone;
import com.kasneb.entity.StudentCourse;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.util.CoreUtil;
import java.io.IOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jikara
 */
@Stateless
public class CentreZoneFacade extends AbstractFacade<CentreZone> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CentreZoneFacade() {
        super(CentreZone.class);
    }

    public List<CentreZone> findZones(StudentCourse studentCourse) throws IOException, CustomHttpException {
        List<CentreZone> zones = CoreUtil.getZones(studentCourse.getCourse());
//        for (CentreZone z : zones) {
//            em.merge(z);
//        }
        return zones;
    }

    public List<CentreZone> findZones() throws IOException, CustomHttpException {
        List<CentreZone> zones = CoreUtil.getZones();
//        for (CentreZone z : zones) {
//            em.merge(z);
//        }
        return zones;
    }

}
