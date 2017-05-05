/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.KasnebCourse;
import com.kasneb.entity.Paper;
import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.StudentCoursePaper;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jikara
 */
@Stateless
public class StudentCoursePaperFacade extends AbstractFacade<StudentCoursePaper> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentCoursePaperFacade() {
        super(StudentCoursePaper.class);
    }

    public List<StudentCoursePaper> getElligiblePapers(StudentCourse studentCourse) {
        KasnebCourse dbCourse = em.find(KasnebCourse.class, studentCourse.getCourse().getId());
        List<StudentCoursePaper> elligiblePapers = new ArrayList<>();
        for (Paper paper : dbCourse.getPapers()) {
            elligiblePapers.add(new StudentCoursePaper(studentCourse, paper));
        }
        return elligiblePapers;
    }

}
