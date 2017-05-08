/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.ExamInvoiceDetail;
import com.kasneb.entity.Exemption;
import com.kasneb.entity.ExemptionInvoiceDetail;
import com.kasneb.entity.ExemptionPaper;
import com.kasneb.entity.Fee;
import com.kasneb.entity.FeeCode;
import com.kasneb.entity.Invoice;
import com.kasneb.entity.InvoiceDetail;
import com.kasneb.entity.InvoiceStatus;
import com.kasneb.entity.KasnebCourse;
import com.kasneb.entity.Level;
import com.kasneb.entity.Paper;
import com.kasneb.entity.Part;
import com.kasneb.entity.RenewalInvoiceDetail;
import com.kasneb.entity.Section;
import com.kasneb.entity.Sitting;
import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.StudentCourseSitting;
import com.kasneb.entity.VerificationStatus;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.util.Constants;
import com.kasneb.util.DateUtil;
import com.kasneb.util.GeneratorUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import org.joda.time.Period;

/**
 *
 * @author jikara
 */
@Stateless
public class InvoiceFacade extends AbstractFacade<Invoice> {
    
    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    @EJB
    com.kasneb.session.FeeFacade feeTypeFacade;
    @EJB
    com.kasneb.session.StudentCourseFacade studentCourseFacade;
    @EJB
    com.kasneb.session.StudentCourseSittingFacade studentCourseSittingFacade;
    @EJB
    com.kasneb.session.ExemptionFacade exemptionFacade;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public InvoiceFacade() {
        super(Invoice.class);
    }
    
    public Invoice generateRegistrationInvoice(StudentCourse entity) throws CustomHttpException, IOException {
        StudentCourse managed = studentCourseFacade.find(entity.getId());
        Iterator<Invoice> iter = managed.getInvoices().iterator();
        while (iter.hasNext()) {
            Invoice existing = iter.next();
            if (existing.getFeeCode().getCode().equals(Constants.REGISTRATION_FEE) && existing.getStatus().getStatus().equals("PENDING")) {
                iter.remove();
                existing.setStatus(new InvoiceStatus("CANCELLED"));
            }
            em.merge(existing);
        }
        KasnebCourse course = em.find(KasnebCourse.class, managed.getCourse().getId());
        Sitting firstSitting = em.find(Sitting.class, entity.getFirstSitting().getId());
        BigDecimal kesTotal = new BigDecimal(0), usdTotal = new BigDecimal(0), gbpTotal = new BigDecimal(0);
        //Generate invoice
        Invoice invoice = new Invoice(GeneratorUtil.generateInvoiceNumber(), new Date());
        Fee adminFee = feeTypeFacade.getAdministrativeFee();
        if (new Date().after(firstSitting.getRegistrationDeadline())) { //is late
            Fee lateFee = feeTypeFacade.getLateCourseRegistrationFee(course);
            invoice.addInvoiceDetail(new InvoiceDetail(lateFee.getKesAmount(), lateFee.getUsdAmount(), lateFee.getGbpAmount(), "Late registration fee"));
            //Add to totals
            kesTotal = kesTotal.add(lateFee.getKesAmount());
            usdTotal = usdTotal.add(lateFee.getUsdAmount());
            gbpTotal = gbpTotal.add(lateFee.getGbpAmount());
            invoice.setDueDate(firstSitting.getLateRegistrationDeadline());
        } else {
            Fee regFee = feeTypeFacade.getCourseRegistrationFee(course);
            invoice.addInvoiceDetail(new InvoiceDetail(regFee.getKesAmount(), regFee.getUsdAmount(), regFee.getGbpAmount(), "Course registration fee"));
            //Add to totals
            kesTotal = kesTotal.add(regFee.getKesAmount());
            usdTotal = usdTotal.add(regFee.getUsdAmount());
            gbpTotal = gbpTotal.add(regFee.getGbpAmount());
            invoice.setDueDate(firstSitting.getRegistrationDeadline());
        }
        invoice.addInvoiceDetail(new InvoiceDetail(adminFee.getKesAmount(), adminFee.getUsdAmount(), adminFee.getGbpAmount(), "Administrative fee"));
        invoice.setStudentCourse(managed);
        //Add administrative fee
        kesTotal = kesTotal.add(adminFee.getKesAmount());
        usdTotal = usdTotal.add(adminFee.getUsdAmount());
        gbpTotal = gbpTotal.add(adminFee.getGbpAmount());
        
        invoice.setKesTotal(kesTotal);
        invoice.setUsdTotal(usdTotal);
        invoice.setGbpTotal(gbpTotal);
        invoice.setFeeCode(new FeeCode(Constants.REGISTRATION_FEE));
        em.persist(invoice);
        return invoice;
    }
    
    public Invoice generateRenewalInvoice(StudentCourse entity) throws CustomHttpException, ParseException {
        StudentCourse managed = studentCourseFacade.find(entity.getId());
        Integer currentYear = DateUtil.getYear(new Date());
        Date academicYearEnd = DateUtil.getDate("30-06-" + currentYear);
        Integer currentAcademicYear = currentYear - 1;
        if (new Date().after(academicYearEnd)) {
            currentAcademicYear = currentYear;
        }
        //Mark unpaid exemption invoices as null
        Iterator<Invoice> iter = managed.getInvoices().iterator();
        while (iter.hasNext()) {
            Invoice existing = iter.next();
            if ((existing.getFeeCode().getCode().equals(Constants.REGISTRATION_RENEWAL_FEE) || existing.getFeeCode().getCode().equals(Constants.EXAM_ENTRY_FEE)) && existing.getStatus().getStatus().equals("PENDING")) {
                iter.remove();
                existing.setStatus(new InvoiceStatus("CANCELLED"));
            }
            em.merge(existing);
        }
        BigDecimal kesTotal = new BigDecimal(0), usdTotal = new BigDecimal(0), gbpTotal = new BigDecimal(0);
        //Generate invoice
        Invoice invoice = new Invoice(GeneratorUtil.generateInvoiceNumber(), new Date());
        invoice.setInvoiceDetails(this.generateRenewalInvoiceDetails(entity));
        invoice.setDueDate(DateUtil.getDate("30-06-" + currentYear));
        for (InvoiceDetail det : invoice.getInvoiceDetails()) {
            kesTotal = kesTotal.add(det.getKesAmount());
            usdTotal = usdTotal.add(det.getUsdAmount());
            gbpTotal = gbpTotal.add(det.getGbpAmount());
        }
        invoice.setKesTotal(kesTotal);
        invoice.setUsdTotal(usdTotal);
        invoice.setGbpTotal(gbpTotal);
        
        invoice.setFeeCode(new FeeCode(Constants.REGISTRATION_RENEWAL_FEE));
        invoice.setStudentCourse(managed);
        em.persist(invoice);
        return invoice;
    }
    
    public List<InvoiceDetail> generateRenewalInvoiceDetails(StudentCourse entity) throws CustomHttpException, ParseException {
        List<InvoiceDetail> renewalInvoiceDetails = new ArrayList<>();
        StudentCourse managed = studentCourseFacade.find(entity.getId());
        Integer currentYear = DateUtil.getYear(new Date());
        Integer currentAcademicYear = currentYear - 1;
        Date academicYearEnd = DateUtil.getDate("30-06-" + currentYear);
        if (new Date().after(academicYearEnd)) {
            currentAcademicYear = currentYear;
        }
        Integer lastSubscriptionYear = managed.getLastSubscription().getYear();
        BigDecimal kesTotal = new BigDecimal(0), usdTotal = new BigDecimal(0), gbpTotal = new BigDecimal(0);
        Date lastSubscriptionExpiry = DateUtil.getDate("30-06-" + (lastSubscriptionYear + 1));
        Period period = new Period(new org.joda.time.DateTime(lastSubscriptionExpiry), new org.joda.time.DateTime(new Date()));
        Integer years = period.getYears() + 1;
        if (years > 3) {
            Fee reinstatementFee = feeTypeFacade.getRegistrationReactivationFee(managed.getCourse());
            renewalInvoiceDetails.add(new InvoiceDetail(reinstatementFee.getKesAmount(), reinstatementFee.getUsdAmount(), reinstatementFee.getGbpAmount(), "Reinstatement fee "));
            kesTotal = kesTotal.add(reinstatementFee.getKesAmount());
            usdTotal = usdTotal.add(reinstatementFee.getUsdAmount());
            gbpTotal = gbpTotal.add(reinstatementFee.getGbpAmount());
            for (int x = 0; x < 3; x++) {
                Fee renewalFee = feeTypeFacade.getAnnualRegistrationRenewalFee(managed.getCourse(), currentAcademicYear - 1);
                renewalInvoiceDetails.add(new RenewalInvoiceDetail(currentAcademicYear, renewalFee.getKesAmount(), renewalFee.getUsdAmount(), renewalFee.getGbpAmount(), "Annual renewal fee : " + currentAcademicYear + "-" + (currentAcademicYear + 1)));
                kesTotal = kesTotal.add(renewalFee.getKesAmount());
                usdTotal = usdTotal.add(renewalFee.getUsdAmount());
                gbpTotal = gbpTotal.add(renewalFee.getGbpAmount());
                currentAcademicYear = currentAcademicYear - 1;
            }
        } else {
            int index = lastSubscriptionYear + 1;
            for (int x = 0; x < years; x++) {
                Fee renewalFee = feeTypeFacade.getAnnualRegistrationRenewalFee(managed.getCourse(), index);
                renewalInvoiceDetails.add(new RenewalInvoiceDetail(index, renewalFee.getKesAmount(), renewalFee.getUsdAmount(), renewalFee.getGbpAmount(), "Annual renewal fee : " + index + "-" + (index + 1)));
                kesTotal = kesTotal.add(renewalFee.getKesAmount());
                usdTotal = usdTotal.add(renewalFee.getUsdAmount());
                gbpTotal = gbpTotal.add(renewalFee.getGbpAmount());
                index++;
            }
        }
        return renewalInvoiceDetails;
    }
    
    public Invoice generateExemptionInvoice(Exemption entity) throws CustomHttpException {
        //Mark unpaid exemption invoices as null
        Exemption managed = exemptionFacade.find(entity.getId());
        StudentCourse studentCourse = studentCourseFacade.find(managed.getStudentCourse().getId());
        Iterator<Invoice> iter = studentCourse.getInvoices().iterator();
        while (iter.hasNext()) {
            Invoice existing = iter.next();
            if (existing.getFeeCode().getCode().equals(Constants.EXEMPTION_FEE) && existing.getStatus().getStatus().equals("PENDING")) {
                iter.remove();
                existing.setStatus(new InvoiceStatus("CANCELLED"));
            }
            em.merge(existing);
        }
        BigDecimal kesTotal = new BigDecimal(0), usdTotal = new BigDecimal(0), gbpTotal = new BigDecimal(0);
        //Generate invoice 
        Invoice invoice = new Invoice(GeneratorUtil.generateInvoiceNumber(), new Date());
        for (ExemptionPaper exemptionPaper : managed.getPapers()) {
            Paper paper = em.find(Paper.class, exemptionPaper.getPaper().getCode());
            if (exemptionPaper.getVerificationStatus().equals(VerificationStatus.APPROVED) && !exemptionPaper.getPaid()) {
                Fee exemptionFee = feeTypeFacade.getExemptionFee(paper);
                invoice.addInvoiceDetail(new ExemptionInvoiceDetail(exemptionPaper, exemptionFee.getKesAmount(), exemptionFee.getUsdAmount(), new BigDecimal(0), "Exemption Fee | " + paper.getCode()));
                //Add to totals
                kesTotal = kesTotal.add(exemptionFee.getKesAmount());
                usdTotal = usdTotal.add(exemptionFee.getUsdAmount());
                gbpTotal = gbpTotal.add(exemptionFee.getGbpAmount());
            }
        }
        Fee adminFee = feeTypeFacade.getAdministrativeFee();
        invoice.addInvoiceDetail(new InvoiceDetail(adminFee.getKesAmount(), adminFee.getUsdAmount(), adminFee.getGbpAmount(), "Administrative fee"));
        //Add administrative fee
        kesTotal = kesTotal.add(adminFee.getKesAmount());
        usdTotal = usdTotal.add(adminFee.getUsdAmount());
        gbpTotal = gbpTotal.add(adminFee.getGbpAmount());
        invoice.setStudentCourse(studentCourse);
        invoice.setKesTotal(kesTotal);
        invoice.setUsdTotal(usdTotal);
        invoice.setGbpTotal(gbpTotal);
        invoice.setFeeCode(new FeeCode(Constants.EXEMPTION_FEE));
        managed.setInvoice(invoice);
        em.persist(invoice);
        return invoice;
    }
    
    public Invoice generateExamEntryInvoice(StudentCourseSitting entity, Map<String, Collection<Paper>> map) throws CustomHttpException, ParseException {
        boolean isLate = false;
        StudentCourseSitting managed = em.find(StudentCourseSitting.class, entity.getId());//Mark unpaid exemption invoices as null
        Iterator<Invoice> iter = managed.getStudentCourse().getInvoices().iterator();
        while (iter.hasNext()) {
            Invoice existing = iter.next();
            if ((existing.getFeeCode().getCode().equals(Constants.EXAM_ENTRY_FEE) || existing.getFeeCode().getCode().equals(Constants.REGISTRATION_RENEWAL_FEE)) && existing.getStatus().getStatus().equals("PENDING")) {
                iter.remove();
                existing.setStatus(new InvoiceStatus("CANCELLED"));
            }
            em.merge(existing);
        }
        BigDecimal kesTotal = new BigDecimal(0), usdTotal = new BigDecimal(0), gbpTotal = new BigDecimal(0);
        //Generate invoice
        Invoice invoice = new Invoice(GeneratorUtil.generateInvoiceNumber(), new Date());
        invoice.setDueDate(managed.getSitting().getExamEntryDeadline());
        if (new Date().after(managed.getSitting().getExamEntryDeadline())) {
            isLate = true;
            invoice.setDueDate(managed.getSitting().getLateExamEntryDeadline());
        }
        invoice.setStudentCourse(managed.getStudentCourse());
        for (Map.Entry<String, Collection<Paper>> entry : map.entrySet()) {
            String key = entry.getKey();
            Collection<Paper> papersCollection = entry.getValue();
            List<Paper> papers = new ArrayList<>();
            papers.addAll(papersCollection);
            String description = "Examination Fee";
            switch (key) {
                case "PER_PART": {
                    Part part = papers.get(0).getSection().getPart();
                    if (isLate) {
                        description = "Late Examination Fee";
                    }
                    for (Section section : part.getSectionCollection()) {
                        Fee fee = feeTypeFacade.getExamEntryFeePerSection(section, isLate);
                        invoice.addInvoiceDetail(new ExamInvoiceDetail(null, section, null, fee.getKesAmount(), fee.getUsdAmount(), fee.getGbpAmount(), description + " | " + section.getName()));
                        kesTotal = kesTotal.add(fee.getKesAmount());
                        usdTotal = usdTotal.add(fee.getUsdAmount());
                        gbpTotal = gbpTotal.add(fee.getGbpAmount());
                    }
                }
                break;
                case "PER_SECTION": {
                    Section section = papers.get(0).getSection();
                    Fee fee = feeTypeFacade.getExamEntryFeePerSection(section, isLate);
                    kesTotal = fee.getKesAmount();
                    usdTotal = fee.getUsdAmount();
                    gbpTotal = fee.getGbpAmount();
                    
                    if (isLate) {
                        description = "Late Examination Fee";
                    }
                    invoice.addInvoiceDetail(new ExamInvoiceDetail(null, section, null, kesTotal, usdTotal, gbpTotal, description + " | " + section.getName()));
                }
                break;
                case "PER_LEVEL": {
                    Level level = papers.get(0).getLevel();
                    Fee fee = feeTypeFacade.getExamEntryFeePerLevel(level, isLate);
                    kesTotal = fee.getKesAmount();
                    usdTotal = fee.getUsdAmount();
                    gbpTotal = fee.getGbpAmount();
                    if (isLate) {
                        description = "Late Examination Fee";
                    }
                    invoice.addInvoiceDetail(new ExamInvoiceDetail(null, null, level, kesTotal, usdTotal, gbpTotal, description + " | " + level.getName()));
                }
                break;
                case "PER_PAPER": {
                    for (Paper paper : papers) {
                        Fee fee = feeTypeFacade.getExamEntryFeePerPaper(paper, isLate);
                        kesTotal = kesTotal.add(fee.getKesAmount());
                        usdTotal = usdTotal.add(fee.getUsdAmount());
                        gbpTotal = gbpTotal.add(fee.getGbpAmount());
                        if (isLate) {
                            description = "Late Examination Fee";
                        }
                        invoice.addInvoiceDetail(new ExamInvoiceDetail(paper, null, null, fee.getKesAmount(), fee.getUsdAmount(), fee.getGbpAmount(), description + " | " + paper.getCode()));
                    }
                }
                break;
            }
        }
        if (!managed.getStudentCourse().isUptoDate()) {  //Student is not upto date
            List<InvoiceDetail> renInvDetails = generateRenewalInvoiceDetails(managed.getStudentCourse());
            for (InvoiceDetail invDetail : renInvDetails) {
                invoice.addInvoiceDetail(invDetail);
                kesTotal = kesTotal.add(invDetail.getKesAmount());
                usdTotal = usdTotal.add(invDetail.getUsdAmount());
                gbpTotal = gbpTotal.add(invDetail.getGbpAmount());
            }
        }
        Fee adminFee = feeTypeFacade.getAdministrativeFee();
        invoice.addInvoiceDetail(new InvoiceDetail(adminFee.getKesAmount(), adminFee.getUsdAmount(), adminFee.getGbpAmount(), "Administrative fee"));
        //Add administrative fee
        kesTotal = kesTotal.add(adminFee.getKesAmount());
        usdTotal = usdTotal.add(adminFee.getUsdAmount());
        gbpTotal = gbpTotal.add(adminFee.getGbpAmount());
        //Set totals
        invoice.setKesTotal(kesTotal);
        invoice.setUsdTotal(usdTotal);
        invoice.setGbpTotal(gbpTotal);
        invoice.setFeeCode(new FeeCode(Constants.EXAM_ENTRY_FEE));
        return invoice;
    }
    
    public Invoice getRenewalInvoice(StudentCourse managed) {
        TypedQuery<Invoice> query = em.createQuery("SELECT i FROM Invoice i WHERE i.studentCourse =:studentCourse AND i.feeCode =:feeCode AND i.status =:status", Invoice.class);
        query.setParameter("studentCourse", managed);
        query.setParameter("feeCode", new FeeCode(Constants.REGISTRATION_RENEWAL_FEE));
        query.setParameter("status", new InvoiceStatus("PENDING"));
        query.setMaxResults(1);
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return null;
        }
    }
    
    public Invoice getExemptionInvoice(StudentCourse managed) throws CustomHttpException {
        TypedQuery<Invoice> query = em.createQuery("SELECT i FROM Invoice i WHERE i.studentCourse =:studentCourse AND i.feeCode =:feeCode AND i.status =:status", Invoice.class);
        query.setParameter("studentCourse", managed);
        query.setParameter("feeCode", new FeeCode(Constants.EXEMPTION_FEE));
        query.setParameter("status", new InvoiceStatus("PENDING"));
        query.setMaxResults(1);
        try {
            return query.getSingleResult();
        } catch (javax.persistence.NoResultException ex) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invoice does not exist");
        }
    }
    
    public Invoice findByReference(String reference) throws CustomHttpException {
        TypedQuery<Invoice> query = em.createQuery("SELECT i FROM Invoice i WHERE i.reference =:reference ", Invoice.class);
        query.setParameter("reference", reference);
        query.setMaxResults(1);
        try {
            Invoice invoice = query.getSingleResult();
            if (invoice == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Bill does not exist");
            }
            if (invoice.getStatus().getStatus().equals("CANCELLED")) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Bill has been cancelled");
            }
            if (invoice.getStatus().getStatus().equals("PAID")) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Bill already paid");
            }
            return invoice;
        } catch (javax.persistence.NoResultException ex) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invoice does not exist");
        }
    }
    
}
