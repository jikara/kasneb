/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.Paper;
import com.kasneb.entity.Section;
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
public class SectionFacade extends AbstractFacade<Section> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SectionFacade() {
        super(Section.class);
    }

    @Override
    public Section find(Object pk) {
        try {
            TypedQuery<Section> query = em.createQuery("select s from Section s left join fetch s.paperCollection WHERE s.sectionPK =:pk", Section.class);
            TypedQuery<Paper> query2 = em.createQuery("select p from Paper p WHERE p.section.sectionPK =:pk", Paper.class);
            query.setParameter("pk", pk);
            query2.setParameter("pk", pk);
            Section section = query.getSingleResult();
            List<Paper> papers=query2.getResultList();
            section.setPaperCollection(papers);
            return section;
        } catch (javax.persistence.NoResultException ex) {
            return null;
        }
    }

}
