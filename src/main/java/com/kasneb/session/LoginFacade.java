/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.Login;
import com.kasneb.entity.Login_;
import com.kasneb.entity.StudentCourse;
import com.kasneb.exception.CustomHttpException;
import java.util.Date;
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
    com.kasneb.session.StudentCourseFacade studentCourseFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LoginFacade() {
        super(Login.class);
    }

    public Login loginStudent(Login login) throws CustomHttpException {
        try {
            String email = login.getEmail();
            String password = login.getPassword();
            if (email == null || email.equals("")) {
                throw new CustomHttpException(Response.Status.BAD_REQUEST, "Email is required");
            }
            if (password == null || password.equals("")) {
                throw new CustomHttpException(Response.Status.BAD_REQUEST, "Password is required");
            }
            Query query = getEntityManager().
                    createQuery("SELECT l FROM Login l WHERE l.email=:email");
            query.setParameter("email", email);
            login = (Login) query.getSingleResult();
            if (login.getStudent() == null) {
                throw new CustomHttpException(Response.Status.FORBIDDEN, "Invalid student login details");
            }
            if (!login.getPassword().equals(password)) {
                throw new CustomHttpException(Response.Status.FORBIDDEN, "Invalid password");
            }
            if (!login.getActivated()) {
                throw new CustomHttpException(Response.Status.FORBIDDEN, "Account is not activated");
            }
            if (login.getBanned()) {
                throw new CustomHttpException(Response.Status.FORBIDDEN, "Account is banned");
            }
            //Routine check for next renewal
            StudentCourse active = studentCourseFacade.findActiveCourse(login.getStudent());
            if (active != null && new Date().after(active.getCurrentSubscription().getExpiry())) {
                studentCourseFacade.prepareNextRenewal(active);
            }
        } catch (NoResultException e) {
            throw new CustomHttpException(Response.Status.FORBIDDEN, "User not found");
        } catch (java.lang.NullPointerException e) {
            throw new CustomHttpException(Response.Status.BAD_REQUEST, "No login details submitted");
        }
        return login;
    }

    public Login loginUser(Login login) throws CustomHttpException {
        try {
            String email = login.getEmail();
            String password = login.getPassword();
            if (email == null || email.equals("")) {
                throw new CustomHttpException(Response.Status.BAD_REQUEST, "Email is required");
            }
            if (password == null || password.equals("")) {
                throw new CustomHttpException(Response.Status.BAD_REQUEST, "Password is required");
            }
            Query query = getEntityManager().
                    createQuery("SELECT l FROM Login l WHERE l.email=:email"); 
            query.setParameter("email", email);
            login = (Login) query.getSingleResult();
            if (login.getUser() == null) {
                throw new CustomHttpException(Response.Status.FORBIDDEN, "Invalid student login details");
            }
            if (!login.getPassword().equals(password)) {
                throw new CustomHttpException(Response.Status.FORBIDDEN, "Invalid password");
            }
            if (!login.getActivated()) {
                throw new CustomHttpException(Response.Status.FORBIDDEN, "Account is not activated");
            }
            if (login.getBanned()) {
                throw new CustomHttpException(Response.Status.FORBIDDEN, "Account is banned");
            }
        } catch (NoResultException e) {
            throw new CustomHttpException(Response.Status.FORBIDDEN, "User not found");
        } catch (java.lang.NullPointerException e) {
            throw new CustomHttpException(Response.Status.BAD_REQUEST, "No login details submitted");
        }
        return login;
    }

    public Login findByEmail(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(Login.class);
        Root<Login> login = cq.from(Login.class);
        cq.where(cb.equal(login.get(Login_.email), email));
        TypedQuery<Login> query = em.createQuery(cq);
        return query.getSingleResult();
    }

}
