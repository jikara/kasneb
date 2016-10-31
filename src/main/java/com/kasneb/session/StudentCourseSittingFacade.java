/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.ExamCentre;
import com.kasneb.entity.Invoice;
import com.kasneb.entity.KasnebCourse;
import com.kasneb.entity.Level;
import com.kasneb.entity.Paper;
import com.kasneb.entity.Part;
import com.kasneb.entity.Section;
import com.kasneb.entity.Sitting;
import com.kasneb.entity.SittingPeriod;
import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.StudentCourseSitting;
import com.kasneb.entity.StudentCourseSittingPaper;
import com.kasneb.entity.StudentCourseSittingStatus;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.model.Email;
import com.kasneb.util.EmailUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;

/**
 *
 * @author jikara
 */
@Stateless
public class StudentCourseSittingFacade extends AbstractFacade<StudentCourseSitting> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    @EJB
    com.kasneb.session.FeeFacade feeTypeFacade;
    @EJB
    com.kasneb.session.CourseFacade courseFacade;
    @EJB
    com.kasneb.session.KasnebCourseFacade kasnebCourseFacade;
    @EJB
    com.kasneb.session.InvoiceFacade invoiceFacade;
    @EJB
    com.kasneb.session.SittingFacade sittingFacade;
    @Resource
    private SessionContext ctx;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentCourseSittingFacade() {
        super(StudentCourseSitting.class);
    }

    /**
     *
     * @param entity
     * @throws com.kasneb.exception.CustomHttpException
     */
    public void register(StudentCourseSitting entity) throws CustomHttpException {
        Map<String, Collection<Paper>> map;
        Invoice examEntryInvoice;
        if (entity.getPapers() != null && entity.getPapers().size() > 0) {
            entity.getPapers().stream().forEach((paper) -> {
                //Get managed
                StudentCourseSitting managed = em.find(StudentCourseSitting.class, entity.getId());
                entity.addStudentCourseSittingPaper(paper);
            });
            map = getBillingMethod(entity);
            examEntryInvoice = invoiceFacade.generateExamEntryInvoice(entity, map);
            entity.addInvoice(examEntryInvoice);
        }
        super.create(entity);
    }

    public void update(StudentCourseSitting entity) throws CustomHttpException {
        StudentCourseSitting managed = em.find(StudentCourseSitting.class, entity.getId());
        if (managed == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This student sitting is not defined");
        }
        //Check if same sitting is booked
        if (managed.getStatus() == StudentCourseSittingStatus.PAID || managed.getStatus() == StudentCourseSittingStatus.CONFIRMED) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Sitting has already been paid for and cannot be updated.");
        }
        try {
            em.detach(managed);
            super.copy(entity, managed);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(StudentCourseSittingFacade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        managed.setSittingCentre(null);
        Map<String, Collection<Paper>> map;
        Invoice examEntryInvoice;
        if (entity.getPapers() != null && entity.getPapers().size() > 0) {
            entity.getPapers().stream().forEach((paper) -> {
                managed.addStudentCourseSittingPaper(paper);
            });
            map = getBillingMethod(managed);
            examEntryInvoice = invoiceFacade.generateExamEntryInvoice(managed, map);
            managed.setInvoice(examEntryInvoice);
        }
        em.merge(managed);
    }

    public void updateCentre(StudentCourseSitting entity) throws CustomHttpException, MessagingException {
        StudentCourseSitting managed = em.find(StudentCourseSitting.class, entity.getId());
        if (managed == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This student sitting does not exist");
        }
        if (!"PAID".equals(managed.getInvoice().getStatus().getStatus())) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Payment must be made before booking a sitting");
        }
        //Check if exam centre exists
        if (entity.getSittingCentre() == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Sitting Centre is required");
        }
        ExamCentre examCentre = em.find(ExamCentre.class, entity.getSittingCentre().getCode());
        if (examCentre == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Exam centre does not exist");
        }
        Collection<KasnebCourse> examsOffered = examCentre.getExamsOffered();
        if (!examsOffered.contains(managed.getStudentCourse().getCourse())) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This exam is not offerred in selected centre");
        }
        managed.setSittingCentre(entity.getSittingCentre());
        managed.setStatus(StudentCourseSittingStatus.CONFIRMED);
        //Send email
        String body = "Your examination booking  for " + managed.getStudentCourse().getCourse().getName() + " was successful. Please click on the link below to access your timetable.<br><a>Link</a><br>Kindly note that you will be required to present your timetable and national Id at the examination room.<br>Wishing you success in your examination.";
        EmailUtil.sendEmail(new Email(managed.getStudentCourse().getStudent().getLoginId().getEmail(), "Course registration verification", body));
        em.merge(managed);
    }

    private Map<String, Collection<Paper>> getBillingMethod(StudentCourseSitting entity) throws CustomHttpException {
        Map<String, Collection<Paper>> map = new HashMap<>();
        Set<Part> parts = new HashSet<>();
        Set<Section> sections = new HashSet<>();
        Set<Level> levels = new HashSet<>();
        //Lists
        List<Part> partsList = new ArrayList<>();
        List<Section> sectionsList = new ArrayList<>();
        List<Level> levelsList = new ArrayList<>();
        //Colections
        Collection<Paper> partPapers = new ArrayList<>();
        Collection<Paper> sectionPapers = new ArrayList<>();
        Collection<Paper> levelPapers = new ArrayList<>();
        Collection<Paper> sittingPapers = new ArrayList<>();

        for (StudentCourseSittingPaper studentCourseSittingPaper : entity.getPapers()) {
            Paper paper = em.find(Paper.class, studentCourseSittingPaper.getPaperCode());
            if (paper == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Paper code '" + studentCourseSittingPaper.getPaper() + "' does not exist");
            }
            sittingPapers.add(paper);
            //check course type
            int courseType = paper.getCourse().getCourseTypeCode();
            switch (courseType) {
                case 100:
                    parts.add(paper.getPart());
                    sections.add(paper.getSection());
                    break;
                case 200:
                    levels.add(paper.getLevel());
                    break;
            }
        }
        //Validate tests
        if (parts.size() > 1) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Two parts cannot be combined");
        }
        partsList.addAll(parts);
        for (Part part : partsList) {
            //Check each section
            if (sittingPapers.containsAll(part.getPaperCollection())) {
                map.put("PER_PART", part.getPaperCollection());
            } else {  //Bill per section
                sectionsList.addAll(sections);
                //two sections can be combined
                for (Section section : sectionsList) {
                    //Check each section
                    if (sittingPapers.containsAll(section.getPaperCollection())) {
                        map.put("PER_SECTION", section.getPaperCollection());
                        //remove 
                        sittingPapers.removeAll(section.getPaperCollection());
                    } else {
                        //bill per paper
                        map.put("PER_PAPER", sittingPapers);
                    }
                }
            }
        }
        levelsList.addAll(levels);
        for (Level level : levelsList) {
            //Check each level
            if (sittingPapers.containsAll(level.getPaperCollection())) {
                map.put("PER_LEVEL", level.getPaperCollection());
                //remove 
                sittingPapers.removeAll(level.getPaperCollection());
            } else {
                //bill per paper
                map.put("PER_PAPER", sittingPapers);
            }
        }
        return map;
    }

    public void createStudentCourse(StudentCourseSitting entity) throws CustomHttpException {
        if (entity.getStudentCourse() == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student course id is not defined");
        }
        //Get student course
        StudentCourse studentCourse = em.find(StudentCourse.class, entity.getStudentCourse().getId());
        if (studentCourse == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student course does not exist");
        }
        //Get sitting 
        Sitting sitting = em.find(Sitting.class, entity.getSitting().getId());
        if (sitting == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Sitting does not exist");
        }
        //Check if same sitting is booked
        Sitting s = sittingFacade.find(entity.getSitting().getSittingPeriod(), entity.getSitting().getSittingYear());
        if (s != null) {
            StudentCourseSitting paidOrConfirmed = this.getPaidOrCOnfirmed(s);
            if (paidOrConfirmed != null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Sitting has already been paid for and cannot be updated.");
            }
        }
        super.create(entity);
    }

    public List<StudentCourseSitting> findAll(Integer courseTypeCode, String courseId, Integer year, SittingPeriod month) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE ty.code =:courseTypeCode AND c.id =:courseId AND ss.sittingYear =:year AND ss.sittingPeriod =:month", StudentCourseSitting.class);
        query.setParameter("courseTypeCode", courseTypeCode);
        query.setParameter("courseId", courseId);
        query.setParameter("year", year);
        query.setParameter("month", month);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAll(Integer courseTypeCode) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE ty.code =:courseTypeCode", StudentCourseSitting.class);
        query.setParameter("courseTypeCode", courseTypeCode);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAll(String courseId) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE  c.id =:courseId ", StudentCourseSitting.class);
        query.setParameter("courseId", courseId);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAllByYear(Integer year) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE ss.sittingYear =:year", StudentCourseSitting.class);
        query.setParameter("year", year);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAll(SittingPeriod month) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE ss.sittingPeriod =:month", StudentCourseSitting.class);
        query.setParameter("month", month);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAll(Integer courseTypeCode, Integer year) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE ty.code =:courseTypeCode AND ss.sittingYear =:year", StudentCourseSitting.class);
        query.setParameter("courseTypeCode", courseTypeCode);
        query.setParameter("year", year);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAll(Integer courseTypeCode, SittingPeriod month) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE ty.code =:courseTypeCode AND ss.sittingPeriod =:month", StudentCourseSitting.class);
        query.setParameter("courseTypeCode", courseTypeCode);
        query.setParameter("month", month);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAll(String courseId, Integer year) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE c.id =:courseId AND ss.sittingYear =:year", StudentCourseSitting.class);
        query.setParameter("courseId", courseId);
        query.setParameter("year", year);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAll(String courseId, SittingPeriod month) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE c.id =:courseId AND ss.sittingPeriod =:month", StudentCourseSitting.class);
        query.setParameter("courseId", courseId);
        query.setParameter("month", month);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAll(String courseId, Integer year, SittingPeriod month) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE c.id =:courseId AND ss.sittingYear =:year AND ss.sittingPeriod =:month", StudentCourseSitting.class);
        query.setParameter("courseId", courseId);
        query.setParameter("year", year);
        query.setParameter("month", month);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAll(Integer courseTypeCode, Integer year, SittingPeriod month) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE ty.code =:courseTypeCode AND ss.sittingYear =:year AND ss.sittingPeriod =:month", StudentCourseSitting.class);
        query.setParameter("courseTypeCode", courseTypeCode);
        query.setParameter("year", year);
        query.setParameter("month", month);
        return query.getResultList();
    }

    public Object findAllByYear(Integer year, SittingPeriod month) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE ss.sittingYear =:year AND ss.sittingPeriod =:month", StudentCourseSitting.class);
        query.setParameter("year", year);
        query.setParameter("month", month);
        return query.getResultList();
    }

    public StudentCourseSitting getPaidOrCOnfirmed(Sitting sitting) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s WHERE s.sitting =:sitting", StudentCourseSitting.class);
        query.setParameter("sitting", sitting);
        return query.getSingleResult();
    }

}
