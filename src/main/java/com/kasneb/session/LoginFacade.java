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
import com.kasneb.entity.StudentCourseStatus;
import com.kasneb.entity.Synchronization;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.util.PredicateUtil;
import com.kasneb.util.SecurityUtil;
import java.io.IOException;
import java.text.ParseException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.Response;

/**
 *
 * @author jikara
 */
@Stateless
public class LoginFacade extends AbstractFacade<Login> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    @EJB
    com.kasneb.session.StudentFacade studentFacade;
    @EJB
    com.kasneb.session.StudentCourseSubscriptionFacade studentCourseSubscriptionFacade;
    @EJB
    com.kasneb.session.NotificationFacade notificationFacade;
    @EJB
    com.kasneb.session.CommunicationFacade communicationFacade;
    @EJB
    com.kasneb.session.SynchronizationFacade synchronizationFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LoginFacade() {
        super(Login.class);
    }

    public Login loginStudent(Login login) throws CustomHttpException, IOException, ParseException {
        String email = login.getEmail();
        String phoneNumber = login.getPhoneNumber();
        String password = login.getPassword();
        if (!PredicateUtil.isSet(email) && !PredicateUtil.isSet(phoneNumber)) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Email or phone number is required");
        }
        if (!PredicateUtil.isSet(password)) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Password is required");
        }
        login = findByEmail(email);
        if (login == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Email does not exist");
        }
        if (login.getStudent() == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invalid student login details");
        }
        if (!login.getPassword().equals(password)) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invalid password");
        }
        if (!login.isEmailActivated() && !login.isPhoneNumberActivated()) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Account is not activated");
        }
        if (login.getBanned()) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Account is banned");
        }
        if (login.getStudent() != null && login.getStudent().getCurrentCourse() != null) {
            if (login.getStudent().getCurrentCourse().getCourseStatus().equals(StudentCourseStatus.ACTIVE)) {
               synchronizationFacade.doSynch(login.getStudent());
            }
        }
        return login;
    }

    public Login loginUser(Login login) throws CustomHttpException {
        try {
            String email = login.getEmail();
            String password = login.getPassword();
            if (email == null || email.equals("")) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Email is required");
            }
            if (password == null || password.equals("")) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Password is required");
            }
            Query query = getEntityManager().createQuery("SELECT l FROM Login l WHERE l.email=:email AND l.user IS NOT NULL");
            query.setParameter("email", email);
            query.setMaxResults(1);
            login = (Login) query.getSingleResult();
            if (login.getUser() == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invalid user login details");
            }
            if (!login.getPassword().equals(password)) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invalid password");
            }
            if (!login.isEmailActivated() && !login.isPhoneNumberActivated()) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Account is not activated");
            }
            if (login.getBanned()) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Account is banned");
            }
        } catch (NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "User not found");
        } catch (java.lang.NullPointerException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "No login details submitted");
        }
        return login;
    }
    

    public Login findByEmail(String email) {
        try {
            TypedQuery<Integer> query = em.createQuery("SELECT l.id FROM Login l WHERE l.email =:email", Integer.class);
            query.setParameter("email", email);
            query.setMaxResults(1);
            Integer loginId = query.getSingleResult();
            return super.find(loginId);
        } catch (javax.persistence.NoResultException ex) {
            return null;
        }
    }

    public void sendStudentEmailResetToken(Login login) throws CustomHttpException {
        login = findByEmail(login.getEmail());
        if (login == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Email does not exist");
        }
        String key = SecurityUtil.createJWT(login.getId(), "Kasneb", "Verification Key", 1000 * 60 * 60 * 24 * 367);
        login.setResetToken(key);
        em.merge(login);
        Communication communication = new Communication(login.getStudent(), null, null, null, CommunicationType.PASSWORD_RESET, AlertType.EMAIL, false);
        communicationFacade.create(communication);
    }

    public void sendStudentSmsResetToken(Login login) throws CustomHttpException {
        login = findByPhone(login.getPhoneNumber());
        if (login == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Phone number does not exist");
        }
        String key = SecurityUtil.createSmsResetToken();
        login.setSmsResetToken(key);
        em.merge(login);
        Communication communication = new Communication(login.getStudent(), null, null, null, CommunicationType.PASSWORD_RESET, AlertType.SMS, false);
        communicationFacade.create(communication);
    }

    public Login resetStudentPasswordSms(Login login) throws CustomHttpException {
        Login managed = null;
        TypedQuery<Login> query = getEntityManager().createQuery("SELECT l FROM Login l WHERE l.smsResetToken =:resetToken", Login.class);
        query.setParameter("resetToken", login.getSmsResetToken());
        query.setMaxResults(1);
        try {
            managed = query.getSingleResult();
            managed.setPassword(login.getPassword());
            managed.setPhoneNumberActivated(true);
            managed.setSmsResetToken(null);
        } catch (NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Token does not exist");
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Expired token");
        }
        return managed;
    }

    public Login resetStudentPasswordEmail(Login login) throws CustomHttpException {
        Login managed = null;
        try {
            int loginId = SecurityUtil.parseJWT(login.getResetToken());
            managed = super.find(loginId);
            managed.setPassword(login.getPassword());
            managed.setEmailActivated(true);
            managed.setResetToken(null);
        } catch (NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Token does not exist");
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Expired token");
        }
        return managed;
    }

    public Login findByPhone(String phoneNumber) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery(Login.class);
            Root<Login> login = cq.from(Login.class);
            cq.where(cb.equal(login.get("phoneNumber"), phoneNumber));
            TypedQuery<Login> query = em.createQuery(cq);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ex) {
            return null;
        }
    }

    public Login findByPhoneOrEmail(String phoneNumber, String email) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery(Login.class);
            Root<Login> login = cq.from(Login.class);
            cb.or(cb.equal(login.get("email"), email), cb.equal(login.get("phoneNumber"), phoneNumber));
            TypedQuery<Login> query = em.createQuery(cq);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ex) {
            return null;
        }
    }

    public Login findById(Integer id) {
        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Login> cq = builder.createQuery(Login.class);
            // write the Root, Path elements as usual
            Root<Login> root = cq.from(Login.class);
            cq.where(builder.equal(root.get("id"), id));
            TypedQuery<Login> query = em.createQuery(cq);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ex) {
            return null;
        }
    }

}
