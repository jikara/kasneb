/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.Level;
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
public class LevelFacade extends AbstractFacade<Level> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LevelFacade() {
        super(Level.class);
    }

    @Override
    public Level find(Object pk) {
        try {
            TypedQuery<Level> query = em.createQuery("select l from Level l left join fetch l.paperCollection WHERE l.levelPK =:pk", Level.class);
            TypedQuery<Paper> query2 = em.createQuery("select p from Paper p WHERE p.level.levelPK =:pk", Paper.class);
            query.setParameter("pk", pk);
            query2.setParameter("pk", pk);
            Level level = query.getSingleResult();
            List<Paper> papers = query2.getResultList();
            level.setPaperCollection(papers);
            return level;
        } catch (javax.persistence.NoResultException ex) {
            return null;
        }
    }

}
