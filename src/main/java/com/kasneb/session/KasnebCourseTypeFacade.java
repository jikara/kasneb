/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.KasnebCourse;
import com.kasneb.entity.KasnebCourseType;
import com.kasneb.entity.Requirement;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.util.CoreUtil;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jikara
 */
@Stateless
public class KasnebCourseTypeFacade extends AbstractFacade<KasnebCourseType> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    @EJB
    com.kasneb.session.RequirementFacade requirementFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KasnebCourseTypeFacade() {
        super(KasnebCourseType.class);
    }

    public List<KasnebCourseType> findCourseTypes() throws IOException, CustomHttpException {
        List<KasnebCourseType> types = CoreUtil.getCourseTypes();
        List<Requirement> requirements = requirementFacade.findAll();
        for (KasnebCourseType courseType : types) {
            List<KasnebCourse> courses = CoreUtil.getCourses(courseType.getCode());
            courseType.setCourseCollection(courses);
            courseType.setCourseRequirements(requirements);
            em.merge(courseType);
        }
        return types;
    }

}
