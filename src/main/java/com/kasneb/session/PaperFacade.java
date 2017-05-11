/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.KasnebCourse;
import com.kasneb.entity.Paper;
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
public class PaperFacade extends AbstractFacade<Paper> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PaperFacade() {
        super(Paper.class);
    }

    @Override
    public Paper find(Object id) {
        TypedQuery<Paper> query = em.createQuery("SELECT p FROM Paper p WHERE p.code =:code", Paper.class);
        query.setParameter("code", id);
        return query.getSingleResult();
    }

    public Paper findPaper(Object id) {
        TypedQuery<Paper> query = em.createQuery("SELECT p FROM Paper p WHERE p.code =:code", Paper.class);
        query.setParameter("code", id);
        return query.getSingleResult();
    }

    public List<Paper> findBySection(Integer sectionId, KasnebCourse course) {
        TypedQuery<Paper> query = em.createQuery("SELECT p FROM Paper p WHERE p.course =:course AND p.section.id =:sectionId", Paper.class);
        query.setParameter("course", course);
        query.setParameter("sectionId", sectionId);
        return query.getResultList();
    }

}
