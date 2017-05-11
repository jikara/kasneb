/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kasneb.client.Centre;
import com.kasneb.client.ExamBooking;
import com.kasneb.client.ExamEntry;
import com.kasneb.client.ExamPaper;
import com.kasneb.client.ExamPaper.ExamPaperPK;
import com.kasneb.entity.AlertType;
import com.kasneb.entity.Communication;
import com.kasneb.entity.CommunicationType;
import com.kasneb.entity.ExamCentre;
import com.kasneb.entity.Invoice;
import com.kasneb.entity.Level;
import com.kasneb.entity.Paper;
import com.kasneb.entity.PaperStatus;
import com.kasneb.entity.Part;
import com.kasneb.entity.Section;
import com.kasneb.entity.Sitting;
import com.kasneb.entity.SittingPeriod;
import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.StudentCourseSitting;
import com.kasneb.entity.StudentCourseSittingPaper;
import com.kasneb.entity.StudentCourseSittingStatus;
import com.kasneb.entity.pk.StudentCourseSittingPaperPK;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.util.CoreUtil;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    @EJB
    com.kasneb.session.PaymentFacade paymentFacade;
    @EJB
    com.kasneb.session.StudentCourseFacade studentCourseFacade;
    @EJB
    com.kasneb.session.CommunicationFacade communicationFacade;
    @EJB
    com.kasneb.session.SectionFacade sectionFacade;
    @EJB
    com.kasneb.session.LevelFacade levelFacade;
    @EJB
    com.kasneb.session.PaperFacade paperFacade;
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
     * @return
     * @throws com.kasneb.exception.CustomHttpException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public StudentCourseSitting createStudentCourseSitting(StudentCourseSitting entity) throws CustomHttpException, IllegalAccessException, InvocationTargetException {
        StudentCourse studentCourse = studentCourseFacade.find(entity.getStudentCourse().getId());
        Sitting sitting = sittingFacade.find(entity.getSitting().getId());
        StudentCourseSitting managed = find(studentCourse, sitting);
        if (managed == null) {
            return super.edit(entity);
        } else {
            //Check if same sitting is booked
            if (managed.getStatus() == StudentCourseSittingStatus.PAID || managed.getStatus() == StudentCourseSittingStatus.CONFIRMED) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, managed.getSitting().getSittingDescription() + " sitting has already been paid for and details can only be updated after the release of the Examination results");
            }
            // em.detach(managed);
            super.copy(entity, managed);
            return super.edit(managed);
        }
    }

    public StudentCourseSitting update(StudentCourseSitting entity) throws CustomHttpException, IllegalAccessException, InvocationTargetException, IOException, ParseException {
        StudentCourseSitting managed = em.find(StudentCourseSitting.class, entity.getId());
        if (managed == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This student sitting is not defined");
        }
        //Check if same sitting is booked
        if (managed.getStatus() == StudentCourseSittingStatus.PAID || managed.getStatus() == StudentCourseSittingStatus.CONFIRMED) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, managed.getSitting().getSittingDescription() + " sitting has already been paid for and details can only be updated after the release of the Examination results");
        }
        super.copy(entity, managed);
        managed.setSittingCentre(null);
        Map<String, Collection<Paper>> map;
        Set<StudentCourseSittingPaper> papers = new HashSet<>();
        if (entity.getPapers() != null && entity.getPapers().size() > 0) {
            entity.getPapers().stream().forEach((sittingPaper) -> {
                StudentCourseSittingPaperPK pk = new StudentCourseSittingPaperPK(sittingPaper.getPaperCode(), managed.getId());
                sittingPaper.setPk(pk);
                sittingPaper.setPaperStatus(PaperStatus.PENDING);
                sittingPaper = em.merge(sittingPaper);
                papers.add(sittingPaper);
            });
            map = getBillingMethod(papers);
            Invoice invoice = invoiceFacade.generateExamEntryInvoice(managed, map);
            managed.setInvoice(invoice);
            managed.setPapers(papers);
        }
        return em.merge(managed);
    }

    public StudentCourseSitting updateCentre(StudentCourseSitting entity) throws CustomHttpException, MessagingException, IOException, JsonProcessingException, ParseException {
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
        managed.setSittingCentre(entity.getSittingCentre());
        createExamEntry(managed);
        managed.setStatus(StudentCourseSittingStatus.CONFIRMED);
        Communication smsCommunication = new Communication(managed.getStudentCourse().getStudent(), managed.getStudentCourse(), managed, null, CommunicationType.EXAM_APPLICATION, AlertType.SMS, false);
        communicationFacade.create(smsCommunication);
        Communication emailCommunication = new Communication(managed.getStudentCourse().getStudent(), managed.getStudentCourse(), managed, null, CommunicationType.EXAM_APPLICATION, AlertType.EMAIL, false);
        communicationFacade.create(emailCommunication);
        return em.merge(managed);
    }

    private Map<String, Collection<Paper>> getBillingMethod(Set<StudentCourseSittingPaper> papers) throws CustomHttpException {
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

        for (StudentCourseSittingPaper studentCourseSittingPaper : papers) {
            Paper paper = paperFacade.find(studentCourseSittingPaper.getPk().getPaperCode());
            if (paper == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Paper code '" + studentCourseSittingPaper.getPaper() + "' does not exist");
            }
            sittingPapers.add(paper);
            //check course type
            int courseType = paper.getCourse().getCourseTypeCode();
            switch (courseType) {
                case 100:
                    parts.add(paper.getSection().getPart());
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
        //Check each section
        sectionsList.addAll(sections);
        //two sections can be combined
        for (Section section : sectionsList) {
            Section managed = sectionFacade.find(section.getSectionPK());
            //Check each section
            if (sittingPapers.containsAll(managed.getPaperCollection())) {
                map.put("PER_SECTION", managed.getPaperCollection());
                //remove 
                sittingPapers.removeAll(managed.getPaperCollection());
            } else {
                //bill per paper
                map.put("PER_PAPER", sittingPapers);
            }
        }
        levelsList.addAll(levels);
        for (Level level : levelsList) {
            Level managed = levelFacade.find(level.getLevelPK());
            //Check each level
            if (sittingPapers.containsAll(managed.getPaperCollection())) {
                map.put("PER_LEVEL", managed.getPaperCollection());
                //remove 
                sittingPapers.removeAll(managed.getPaperCollection());
            } else {
                //bill per paper
                map.put("PER_PAPER", sittingPapers);
            }
        }
        return map;
    }

    public StudentCourseSitting find(StudentCourse studentCourse, Sitting sitting) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s WHERE s.studentCourse =:studentCourse AND s.sitting =:sitting", StudentCourseSitting.class);
        query.setParameter("studentCourse", studentCourse);
        query.setParameter("sitting", sitting);
        try {
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<StudentCourseSitting> findAll() {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE s.status =:status ORDER BY s.created DESC", StudentCourseSitting.class);
        query.setParameter("status", StudentCourseSittingStatus.CONFIRMED);
        query.setMaxResults(100);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAll(Integer courseTypeCode, String courseId, Integer year, SittingPeriod month) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE ty.code =:courseTypeCode AND c.id =:courseId AND ss.sittingYear =:year AND ss.sittingPeriod =:month AND s.status =:status ORDER BY s.created DESC", StudentCourseSitting.class);
        query.setParameter("courseTypeCode", courseTypeCode);
        query.setParameter("courseId", courseId);
        query.setParameter("year", year);
        query.setParameter("month", month);
        query.setParameter("status", StudentCourseSittingStatus.CONFIRMED);
        query.setMaxResults(100);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAll(Integer courseTypeCode) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE ty.code =:courseTypeCode AND s.status =:status ORDER BY s.created DESC", StudentCourseSitting.class);
        query.setParameter("courseTypeCode", courseTypeCode);
        query.setParameter("status", StudentCourseSittingStatus.CONFIRMED);
        query.setMaxResults(100);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAll(String courseId) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE  c.id =:courseId AND s.status =:status ORDER BY s.created DESC", StudentCourseSitting.class);
        query.setParameter("courseId", courseId);
        query.setParameter("status", StudentCourseSittingStatus.CONFIRMED);
        query.setMaxResults(100);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAllByYear(Integer year) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE ss.sittingYear =:year AND s.status =:status ORDER BY s.created DESC", StudentCourseSitting.class);
        query.setParameter("year", year);
        query.setParameter("status", StudentCourseSittingStatus.CONFIRMED);
        query.setMaxResults(100);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAll(SittingPeriod month) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE ss.sittingPeriod =:month AND s.status =:status ORDER BY s.created DESC", StudentCourseSitting.class);
        query.setParameter("month", month);
        query.setParameter("status", StudentCourseSittingStatus.CONFIRMED);
        query.setMaxResults(100);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAll(Integer courseTypeCode, Integer year) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE ty.code =:courseTypeCode AND ss.sittingYear =:year AND s.status =:status ORDER BY s.created DESC", StudentCourseSitting.class);
        query.setParameter("courseTypeCode", courseTypeCode);
        query.setParameter("year", year);
        query.setParameter("status", StudentCourseSittingStatus.CONFIRMED);
        query.setMaxResults(100);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAll(Integer courseTypeCode, SittingPeriod month) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE ty.code =:courseTypeCode AND ss.sittingPeriod =:month AND s.status =:status ORDER BY s.created DESC", StudentCourseSitting.class);
        query.setParameter("courseTypeCode", courseTypeCode);
        query.setParameter("month", month);
        query.setParameter("status", StudentCourseSittingStatus.CONFIRMED);
        query.setMaxResults(100);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAll(String courseId, Integer year) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE c.id =:courseId AND ss.sittingYear =:year AND s.status =:status ORDER BY s.created DESC", StudentCourseSitting.class);
        query.setParameter("courseId", courseId);
        query.setParameter("year", year);
        query.setParameter("status", StudentCourseSittingStatus.CONFIRMED);
        query.setMaxResults(100);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAll(String courseId, SittingPeriod month) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE c.id =:courseId AND ss.sittingPeriod =:month AND s.status =:status ORDER BY s.created DESC", StudentCourseSitting.class);
        query.setParameter("courseId", courseId);
        query.setParameter("month", month);
        query.setParameter("status", StudentCourseSittingStatus.CONFIRMED);
        query.setMaxResults(100);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAll(String courseId, Integer year, SittingPeriod month) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE c.id =:courseId AND ss.sittingYear =:year AND ss.sittingPeriod =:month AND s.status =:status ORDER BY s.created DESC", StudentCourseSitting.class);
        query.setParameter("courseId", courseId);
        query.setParameter("year", year);
        query.setParameter("month", month);
        query.setParameter("status", StudentCourseSittingStatus.CONFIRMED);
        query.setMaxResults(100);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findAll(Integer courseTypeCode, Integer year, SittingPeriod month) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE ty.code =:courseTypeCode AND ss.sittingYear =:year AND ss.sittingPeriod =:month AND s.status =:status ORDER BY s.created DESC", StudentCourseSitting.class);
        query.setParameter("courseTypeCode", courseTypeCode);
        query.setParameter("year", year);
        query.setParameter("month", month);
        query.setParameter("status", StudentCourseSittingStatus.CONFIRMED);
        query.setMaxResults(100);
        return query.getResultList();
    }

    public Object findAllByYear(Integer year, SittingPeriod month) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s JOIN s.studentCourse sc JOIN sc.course c JOIN c.kasnebCourseType ty JOIN s.sitting ss WHERE ss.sittingYear =:year AND ss.sittingPeriod =:month AND s.status =:status ORDER BY s.created DESC", StudentCourseSitting.class);
        query.setParameter("year", year);
        query.setParameter("month", month);
        query.setParameter("status", StudentCourseSittingStatus.CONFIRMED);
        query.setMaxResults(100);
        return query.getResultList();
    }

    public StudentCourseSitting getPaidOrCOnfirmed(Sitting sitting) {
        TypedQuery<StudentCourseSitting> query = em.createQuery("SELECT s FROM StudentCourseSitting s WHERE s.sitting =:sitting AND s.status =:status ORDER BY s.created DESC", StudentCourseSitting.class);
        query.setParameter("sitting", sitting);
        query.setParameter("status", StudentCourseSittingStatus.CONFIRMED);
        query.setMaxResults(100);
        query.setMaxResults(1);
        return query.getSingleResult();
    }

    public void createExamEntry(StudentCourseSitting studentCourseSitting) throws IOException, JsonProcessingException, CustomHttpException, ParseException {
        StudentCourse studentCourse = studentCourseFacade.find(studentCourseSitting.getStudentCourse().getId());
        Sitting sitting = studentCourseSitting.getSitting();
        String papers = "";
        List<ExamPaper> examPapers = new ArrayList<>();
        List<ExamBooking> examBookings = new ArrayList<>();
        String combined = "";
        Set<Integer> sectionSet = new HashSet<>();
        Integer sectionId = 1;
        Integer partId = 1;
        for (StudentCourseSittingPaper paper : studentCourseSitting.getPapers()) {
            switch (studentCourseSitting.getStudentCourse().getCourse().getCourseTypeCode()) {
                case 100:
                    sectionSet.add(paper.getPaper().getSection().getSectionPK().getId());
                    break;
                case 200:
                    partId = studentCourse.getCurrentLevel().getLevelPK().getId();
                    sectionSet.add(paper.getPaper().getLevel().getLevelPK().getId());
                    break;
            }
            ExamPaperPK pk = new ExamPaper().new ExamPaperPK(studentCourse.getRegistrationNumber(), paper.getPaper().getCode());
            ExamPaper examPaper = new ExamPaper();
            switch (studentCourse.getCourse().getCourseTypeCode()) {
                case 100:
                    examPaper = new ExamPaper(pk, paper.getPaper().getSection().getSectionPK().getId(), null, "N");
                    break;
                case 200:
                    examPaper = new ExamPaper(pk, null, paper.getPaper().getLevel().getLevelPK().getId(), "N");
                    break;
            }
            examPapers.add(examPaper);
        }
        List<Integer> sections = new ArrayList<>();
        sections.addAll(sectionSet);
        Collections.sort(sections);
        for (int intSectionId : sections) {
            String section = String.valueOf(intSectionId);
            combined = combined + section;
        }
        sectionId = Integer.parseInt(combined);
        Integer period = 0;
        switch (studentCourseSitting.getSitting().getSittingPeriod()) {
            case MAY:
                period = 1;
                break;
            case NOVEMBER:
                period = 2;
                break;
        }
        ExamEntry examEntry = new ExamEntry(studentCourse.getRegistrationNumber(), partId, sectionId, sitting.getSittingYear(), period, new Centre(studentCourseSitting.getSittingCentre().getCode()), studentCourseSitting.getInvoice().getPayments().get(0).getReceiptNo(), "Y", 2, papers, papers, examPapers, examBookings);
        //Create receipt 
        paymentFacade.createReceipt(studentCourseSitting.getInvoice());
        //Create Exam entry
        CoreUtil.createExamEntry(examEntry, studentCourse.getCourse());
    }

    public List<com.kasneb.dto.StudentCourseSitting> findByStudentCourse(StudentCourse studentCourse) {
        TypedQuery<com.kasneb.dto.StudentCourseSitting> query = em.createQuery("SELECT new com.kasneb.dto.StudentCourseSitting(s.id,s.sitting) FROM StudentCourseSitting s WHERE s.studentCourse =:studentCourse AND s.status =:status", com.kasneb.dto.StudentCourseSitting.class);
        query.setParameter("studentCourse", studentCourse);
        query.setParameter("status", StudentCourseSittingStatus.CONFIRMED);
        return query.getResultList();
    }

    public List<StudentCourseSitting> findSittings(StudentCourse studentCourse) {
        TypedQuery<StudentCourseSitting> query = getEntityManager().createQuery("SELECT s FROM StudentCourseSitting s WHERE s.studentCourse =:studentCourse", StudentCourseSitting.class);
        query.setParameter("studentCourse", studentCourse);
        return query.getResultList();
    }

}
