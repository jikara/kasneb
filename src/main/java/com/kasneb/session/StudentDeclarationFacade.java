/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.StudentDeclaration;
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
public class StudentDeclarationFacade extends AbstractFacade<StudentDeclaration> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentDeclarationFacade() {
        super(StudentDeclaration.class);
    }

    public List<StudentDeclaration> findAll(Integer id, Boolean response) {
        TypedQuery<StudentDeclaration> query = em.createQuery("SELECT sd FROM StudentDeclaration sd WHERE sd.declaration.id =:id AND sd.response=:response", StudentDeclaration.class);
        query.setParameter("response", response);
        query.setParameter("id", id);
       query.setMaxResults(100);
        return query.getResultList();
    }

    public List<StudentDeclaration> findAll(Integer id) {
        TypedQuery<StudentDeclaration> query = em.createQuery("SELECT sd FROM StudentDeclaration sd WHERE sd.declaration.id =:id", StudentDeclaration.class);
        query.setParameter("id", id);
         query.setMaxResults(100);
        return query.getResultList();
    }

    public List<StudentDeclaration> findAll(Boolean response) {
        TypedQuery<StudentDeclaration> query = em.createQuery("SELECT sd FROM StudentDeclaration sd WHERE sd.response=:response", StudentDeclaration.class);
        query.setParameter("response", response);
        query.setMaxResults(100);
        return query.getResultList();
    }

}
