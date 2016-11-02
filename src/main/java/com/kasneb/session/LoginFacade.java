/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.Login;
import com.kasneb.entity.Login_;
import com.kasneb.entity.Notification;
import com.kasneb.entity.NotificationStatus;
import com.kasneb.entity.NotificationType;
import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.StudentCourseSubscription;
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
    @EJB
    com.kasneb.session.StudentCourseSubscriptionFacade studentCourseSubscriptionFacade;
    @EJB
    com.kasneb.session.NotificationFacade notificationFacade;

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
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Email is required");
            }
            if (password == null || password.equals("")) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Password is required");
            }
            Query query = getEntityManager().
                    createQuery("SELECT l FROM Login l WHERE l.email=:email");
            query.setParameter("email", email);
            login = (Login) query.getSingleResult();
            if (login.getStudent() == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invalid student login details");
            }
            if (!login.getPassword().equals(password)) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invalid password");
            }
            if (!login.getActivated()) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Account is not activated");
            }
            if (login.getBanned()) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Account is banned");
            }
            //Routine check for next renewal
            StudentCourse active = studentCourseFacade.findActiveCourse(login.getStudent());
            StudentCourseSubscription latest = studentCourseSubscriptionFacade.getLastSubscription(active);
            if (active != null && latest!=null && new Date().after(latest.getExpiry())) {
                Notification notification = new Notification(NotificationStatus.UNREAD, NotificationType.DUEDATE, "Your registration has expired.", active.getStudent());
                notificationFacade.create(notification);
            }
        } catch (NoResultException e) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "User not found");
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "No login details submitted");
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
            Query query = getEntityManager().
                    createQuery("SELECT l FROM Login l WHERE l.email=:email");
            query.setParameter("email", email);
            login = (Login) query.getSingleResult();
            if (login.getUser() == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invalid student login details");
            }
            if (!login.getPassword().equals(password)) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invalid password");
            }
            if (!login.getActivated()) {
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
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(Login.class);
        Root<Login> login = cq.from(Login.class);
        cq.where(cb.equal(login.get(Login_.email), email));
        TypedQuery<Login> query = em.createQuery(cq);
        return query.getSingleResult();
    }

}
