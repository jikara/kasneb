/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.SystemStatus;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jikara
 */
@Stateless
public class SystemStatusFacade extends AbstractFacade<SystemStatus> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SystemStatusFacade() {
        super(SystemStatus.class);
    }

    public String getSystemMessage(Integer code) {
        SystemStatus systemStatus = super.find(code);
        if (systemStatus == null) {
            return "An internal error occured.Error description is not available";
        }
        return systemStatus.getDescription();
    }

}
