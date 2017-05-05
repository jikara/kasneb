/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.AlertType;
import com.kasneb.entity.Communication;
import com.kasneb.entity.CommunicationType;
import com.kasneb.entity.Login;
import com.kasneb.entity.User;
import com.kasneb.exception.CustomHttpException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    @EJB
    com.kasneb.session.CommunicationFacade communicationFacade;
    @EJB
    com.kasneb.session.LoginFacade loginFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }

    public List<User> findVerifyUsers() {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    public User createUser(User user) {
        super.create(user);
        Communication communication = new Communication(null, null, null, null, CommunicationType.ADMIN_PASSWORD, AlertType.EMAIL, false);
        communication.setUser(user);
        communicationFacade.create(communication);
        return user;
    }

    public User update(User user) {
        User managed = super.find(user.getId());
        try {
            super.copy(user, managed);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            // Logger.getLogger(UserFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return managed;
    }

    public void changePassword(User entity) throws CustomHttpException, IllegalAccessException, InvocationTargetException {
        if (entity.getCurrentPassword() == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Current password cannot be null");
        }
        if (entity.getNewPassword() == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "New password cannot null");
        }
        if (entity.getCurrentPassword().equals(entity.getNewPassword())) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "New password cannot be same as current password");
        }
        if (entity.getId() == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "User id cannot be null");
        }
        Login login = loginFacade.find(entity.getId());
        User managed = login.getUser();
        if (managed == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "User does not exist");
        }
        managed.getLoginId().setPassword(entity.getNewPassword());
        managed.setPasswordChanged(Boolean.TRUE);
        super.edit(managed);
    }

}
