/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.CourseType;
import com.kasneb.entity.KasnebCourse;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author jikara
 */
@Stateless
public class KasnebCourseFacade extends AbstractFacade<KasnebCourse> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KasnebCourseFacade() {
        super(KasnebCourse.class);
    }

    public Collection<KasnebCourse> findByType(Integer code) {
        TypedQuery<KasnebCourse> query
                = em.createNamedQuery("KasnebCourse.findByCourseType", KasnebCourse.class);
        query.setParameter("courseType", new CourseType(code));
        return query.getResultList();
    }

}
