/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.CourseType;
import com.kasneb.entity.Course_;
import com.kasneb.entity.KasnebCourse;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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

    public Collection<KasnebCourse> findKasnebCourses() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<KasnebCourse> cq = builder.createQuery(KasnebCourse.class);
        // write the Root, Path elements as usual
        Root<KasnebCourse> root = cq.from(KasnebCourse.class);
        cq.multiselect(root.get(Course_.id), root.get(Course_.name));  //using metamodel
        return em.createQuery(cq).getResultList();
    }
    
     public Collection<KasnebCourse> findKasnebCourses1() {
        TypedQuery<KasnebCourse> query = em.createQuery("SELECT c FROM KasnebCourse c left join fetch c.papers", KasnebCourse.class);
        Collection<KasnebCourse> courses =query.getResultList();
        return courses;
    }
}
