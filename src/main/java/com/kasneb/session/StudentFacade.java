/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.Invoice;
import com.kasneb.entity.Student;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.util.SecurityUtil;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author jikara
 */
@Stateless
public class StudentFacade extends AbstractFacade<Student> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentFacade() {
        super(Student.class);
    }

    public void verifyAccount(String verificationToken) throws CustomHttpException {
        Student student;
        Query query = getEntityManager().createQuery("SELECT s FROM Student s WHERE s.loginId.verificationToken=:verificationToken");
        query.setParameter("verificationToken", verificationToken);
        try {
            Integer studentId = SecurityUtil.parseJWT(verificationToken);
            student = getEntityManager().find(Student.class, studentId);
            if (student.getLoginId().getActivated()) {
                throw new CustomHttpException(Status.NOT_ACCEPTABLE, "Account already activated");
            }
            student = (Student) query.getSingleResult();
            try {
                student.getLoginId().setActivated(true); //Mark as activated
                student.getLoginId().setVerificationToken(null);//Set verification key to null
                getEntityManager().merge(student);
            } catch (io.jsonwebtoken.ExpiredJwtException ex) {
                throw new CustomHttpException(Status.FORBIDDEN, "Expired token");
            } catch (Exception ex) {
                throw new CustomHttpException(Status.FORBIDDEN, "Invalid token");
            }
        } catch (NoResultException e) {
            throw new CustomHttpException(Status.FORBIDDEN, "Token does not exist");
        } catch (Exception ex) {
            throw new CustomHttpException(Status.FORBIDDEN, ex.getMessage());
        }
    }

    public void verifySmsAccount(String smsToken) throws CustomHttpException {
        Student student;
        Query query = getEntityManager().createQuery("SELECT s FROM Student s WHERE s.loginId.smsToken =:smsToken");
        query.setParameter("smsToken", smsToken);
        try {
            Integer studentId = SecurityUtil.parseSmsToken(smsToken);
            student = getEntityManager().find(Student.class, studentId);
            if (student.getLoginId().getActivated()) {
                throw new CustomHttpException(Status.NOT_ACCEPTABLE, "Account already activated");
            }
            student = (Student) query.getSingleResult();
            student.getLoginId().setActivated(true); //Mark as activated
            student.getLoginId().setSmsToken(null);
            getEntityManager().merge(student);
        } catch (NoResultException e) {
            throw new CustomHttpException(Status.FORBIDDEN, "Token does not exist");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CustomHttpException(Status.FORBIDDEN, ex.getMessage());
        }
    }

    public List<Invoice> findInvoices(Integer id) {
        TypedQuery<Invoice> query = getEntityManager().createQuery("SELECT i FROM Invoice i WHERE i.studentCourse.student =:student", Invoice.class);
        query.setParameter("student", new Student(id));
        return query.getResultList();
    }

    public Student createStudent(Student entity) throws CustomHttpException {
        TypedQuery<Student> query = getEntityManager().createQuery("SELECT s FROM Student s WHERE s.loginId.email=:email", Student.class);
        query.setParameter("email", entity.getLoginId().getEmail());
        if (query.getResultList().size() > 0) {
            throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Email already taken");
        }
        getEntityManager().persist(entity);
        createWallet(entity);
        return entity;
    }

    public BigDecimal getBalance(Student student) throws CustomHttpException {
        return new BigDecimal("500.27");
    }

    public void createWallet(Student student) throws CustomHttpException {

    }

}
