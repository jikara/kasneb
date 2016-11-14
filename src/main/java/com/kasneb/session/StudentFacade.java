/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.client.Registration;
import com.kasneb.entity.Contact;
import com.kasneb.entity.Country;
import com.kasneb.entity.Course;
import com.kasneb.entity.Invoice;
import com.kasneb.entity.KasnebCourse;
import com.kasneb.entity.KasnebStudentCourseQualification;
import com.kasneb.entity.Part;
import com.kasneb.entity.Sitting;
import com.kasneb.entity.Student;
import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.StudentCourseStatus;
import com.kasneb.entity.StudentCourseSubscription;
import com.kasneb.entity.User;
import com.kasneb.entity.VerificationStatus;
import com.kasneb.entity.pk.PartPK;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.util.CoreUtil;
import com.kasneb.util.SecurityUtil;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
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
        // createWallet(entity);
        return entity;
    }

    public BigDecimal getBalance(Student student) throws CustomHttpException {
        return new BigDecimal("500.27");
    }

    public void createWallet(Student student) throws CustomHttpException {

    }

    public Student updateStudent(Student entity) throws CustomHttpException {
        //Check if student is registered and confirmed  
        if (entity.getId() == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student id is required");
        }
        Student managed = super.find(entity.getId());
        if (managed == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student  does not exist");
        }
        try {
            em.detach(managed);
            super.copy(entity, managed);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(StudentCourseFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        em.merge(managed);
        return managed;
    }

    public List<Student> findAll(Date startDate, Date endDate) {
        TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s WHERE s.created BETWEEN :startDate AND :endDate", Student.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    public Student verifyPreviousStudentCourse(Student entity) throws CustomHttpException, IOException {
        Collection<StudentCourse> studentCourses = new ArrayList<>();
        Registration reg = null;
        switch (entity.getPreviousCourseCode()) {
            case "01":
                reg = CoreUtil.getStudentCourse(entity.getPreviousRegistrationNo(), "cpa");
                break;
            case "02":
                reg = CoreUtil.getStudentCourse(entity.getPreviousRegistrationNo(), "cs");
                break;
        }
        if (reg == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Registration number does not exist");
        }
//        if (!reg.getDateOfBirth().equals(entity.getDob())) {
//            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "No match for date of birth");
//        } 
        //String firstName, String middleName, String lastName, String phoneNumber, String gender, String email
        //Contact(String postalAddress, String postalCode, String town, Student student, Country countryId, County countyId) 

        Student existing = new Student(reg.getFirstName(), reg.getOtherName(), reg.getLastName(), entity.getPhoneNumber(), reg.getSex().getDescription(), entity.getEmail());
        Contact contact = new Contact(reg.getAddress1(), reg.getAddress2(), reg.getAddress3(), null, new Country(reg.getNationality().getCode()), null);//String registrationNumber, Boolean active, Date dateVerified, User verifiedBy, String remarks, VerificationStatus verificationStatus, KasnebCourse course, Student student, Sitting firstSitting, Date nextRenewal
        existing.setContact(contact);
        Sitting firstSitting = new Sitting(1);
        Part currentPart = getCurrentPart(reg, entity.getPreviousCourseCode());
        StudentCourse studentCourse = new StudentCourse(reg.getRegistrationNumber(), true, new Date(), new User(1), "Existing at Kasneb", VerificationStatus.APPROVED, new KasnebCourse("01"), firstSitting, true);
        studentCourse.setCurrentPart(currentPart);
        StudentCourseStatus courseStatus = StudentCourseStatus.ACTIVE;
        if (reg.getEligiblePapers().isEmpty()) {
            Set<KasnebStudentCourseQualification> qs = new HashSet<>();
            courseStatus = StudentCourseStatus.COMPLETED;
            studentCourse.setActive(false);
            //Set as qualification
            qs.add(new KasnebStudentCourseQualification(new Course("01")));
            studentCourse.setKasnebQualifications(qs);
        }
        studentCourse.setCourseStatus(courseStatus);
        studentCourse.setCurrentSubscription(new StudentCourseSubscription(studentCourse, getNextRenewalDate()));
        studentCourses.add(studentCourse);//Add to collection
        existing.setStudentCourses(studentCourses);
        return existing;
    }

    private Part getCurrentPart(Registration reg, String courseCode) {
        return em.find(Part.class, new PartPK(1, courseCode));
    }

    private Date getNextRenewalDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date nextRenewalDate = null;
        Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
        try {
            nextRenewalDate = sdf.parse((currentYear + 1) + "-06-30");
        } catch (ParseException ex) {
            Logger.getLogger(StudentCourseFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return nextRenewalDate;
    }

}
