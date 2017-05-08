/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.client.Registration;
import com.kasneb.entity.KasnebStudentQualification;
import com.kasneb.entity.Sitting;
import com.kasneb.entity.Student;
import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.StudentCourseStatus;
import com.kasneb.entity.Synchronization;
import com.kasneb.entity.pk.StudentQualificationPK;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.util.CoreUtil;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;

/**
 *
 * @author jikara
 */
@Stateless
public class SynchronizationFacade extends AbstractFacade<Synchronization> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    @EJB
    com.kasneb.session.StudentFacade studentFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SynchronizationFacade() {
        super(Synchronization.class);
    }

    public List<Synchronization> getSynchronizations() {
        return super.findAll();
    }

    @Schedule(hour = "*", minute = "*/30", second = "*", persistent = false)
    public void synchronize() {
        List<Synchronization> synchronizations = getSynchronizations();
        for (Synchronization synchronization : synchronizations) {
            doSynch(synchronization);
        }
    }

    public void doSynch(Synchronization synchronization) {
        try {
            Student managed = synchronization.getStudent();
            Collection<StudentCourse> studentCourses = new ArrayList<>();
            StudentCourse currentCourse = managed.getCurrentCourse();
            String sexCode = "1";
            Registration registration = CoreUtil.getStudentCourse(currentCourse.getRegistrationNumber(), currentCourse.getCourse());
            if (registration == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Registration number does not exist");
            }
            if (registration.getSex() != null) {
                switch (registration.getSex().getCode()) {
                    case "M":
                        sexCode = "1";
                        break;
                    case "F":
                        sexCode = "2";
                        break;
                }
            }
            String phoneNumber = registration.getCellphone();
            if (managed.getPhoneNumber() != null) {
                phoneNumber = managed.getPhoneNumber();
            }
            if (managed == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student does not exist");
            }
            Sitting firstSitting = new Sitting(1);
            StudentCourseStatus courseStatus = StudentCourseStatus.ACTIVE;
            if (registration.getEligiblePapers().isEmpty()) {  //IS COMPELETED
                Set<KasnebStudentQualification> qs = new HashSet<>();
                courseStatus = StudentCourseStatus.COMPLETED;
                managed.getCurrentCourse().setActive(false);
                //Set as qualification
                StudentQualificationPK pk = new StudentQualificationPK(managed.getId(), currentCourse.getCourse().getId());
                qs.add(new KasnebStudentQualification(pk));
                managed.setKasnebQualifications(qs);
                currentCourse.setCourseStatus(courseStatus);
                managed.setCurrentCourse(null);
            }
            currentCourse.setCurrentPart(registration.getCurrentPart());
            currentCourse.setCurrentLevel(registration.getCurrentLevel());
            currentCourse.setCourseStatus(courseStatus);
            currentCourse.setStudentCourseSittings(studentFacade.getStudentCourseSittings(currentCourse, registration));//Add sittings        
            //currentCourse.setExemptions(getExemptions(currentCourse, registration));//Add exemptions       
            currentCourse.setSubscriptions(studentFacade.getSubscriptions(currentCourse, registration));  //Add subscriptions
            currentCourse.setPayments(studentFacade.getPayments(currentCourse, registration));  //Add payments
            currentCourse.setElligiblePapers(studentFacade.getElligiblePapers(currentCourse, registration));
            studentCourses.add(currentCourse);//Add to collection
            managed.setStudentCourses(studentCourses);
            em.merge(managed);
        } catch (CustomHttpException | IOException | ParseException ex) {
            // Logger.getLogger(SynchronizationFacade.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            synchronization.setSynched(true);
            super.edit(synchronization);
        }
    }

}
