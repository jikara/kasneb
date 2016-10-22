/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.ExemptionInvoice;
import com.kasneb.entity.ExemptionInvoiceDetail;
import com.kasneb.entity.Fee;
import com.kasneb.entity.FeeCode;
import com.kasneb.entity.Invoice;
import com.kasneb.entity.InvoiceDetail;
import com.kasneb.entity.InvoiceStatus;
import com.kasneb.entity.KasnebCourse;
import com.kasneb.entity.Level;
import com.kasneb.entity.Paper;
import com.kasneb.entity.Part;
import com.kasneb.entity.Section;
import com.kasneb.entity.Sitting;
import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.StudentCourseExemptionPaper;
import com.kasneb.entity.StudentCourseSitting;
import com.kasneb.entity.pk.StudentCourseExemptionPaperPK;
import com.kasneb.exception.CustomHttpException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

    private static final String REGISTRATION_FEE = "REGISTRATION_FEE";

    private static final String REGISTRATION_RENEWAL_FEE = "REGISTRATION_RENEWAL_FEE";

    private static final String EXEMPTION_FEE = "EXEMPTION_FEE";

    private static final String FEE_CODE = "EXAM_ENTRY_FEE";

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InvoiceFacade() {
        super(Invoice.class);
    }

    public Invoice generateRegistrationInvoice(StudentCourse studentCourse) throws CustomHttpException {
        //Mark unpaid exemption invoices as null
        Iterator<Invoice> iter = studentCourse.getInvoices().iterator();
        while (iter.hasNext()) {
            Invoice existing = iter.next();
            if (existing.getFeeCode().getCode().equals(REGISTRATION_FEE) && existing.getStatus().getStatus().equals("PENDING")) {
                iter.remove();
                existing.setStatus(new InvoiceStatus("CANCELLED"));
            }
        }
        KasnebCourse course = em.find(KasnebCourse.class, studentCourse.getCourse().getId());
        Sitting firstSitting = em.find(Sitting.class, studentCourse.getFirstSitting().getId());
        BigDecimal kesTotal = new BigDecimal(0), usdTotal = new BigDecimal(0), gbpTotal = new BigDecimal(0);
        //Generate invoice
        Invoice invoice = new Invoice(UUID.randomUUID().toString(), new Date());
        Fee regFee = feeTypeFacade.getCourseRegistrationFeeType(course);
        invoice.addInvoiceDetail(new InvoiceDetail(regFee.getKesAmount(), regFee.getUsdAmount(), regFee.getGbpAmount(), "Course registration fee"));
        Fee adminFee = feeTypeFacade.getAdministrativeFee();
        invoice.addInvoiceDetail(new InvoiceDetail(adminFee.getKesAmount(), adminFee.getUsdAmount(), adminFee.getGbpAmount(), "Administrative fee"));
        invoice.setStudentCourse(studentCourse);
        //Add to totals
        kesTotal = kesTotal.add(regFee.getKesAmount());
        usdTotal = usdTotal.add(regFee.getUsdAmount());
        gbpTotal = gbpTotal.add(regFee.getGbpAmount());
        if (new Date().after(firstSitting.getRegistrationDeadline())) { //is late
            Fee lateFee = feeTypeFacade.getLateCourseRegistrationFeeType(course);
            invoice.addInvoiceDetail(new InvoiceDetail(lateFee.getKesAmount(), lateFee.getUsdAmount(), lateFee.getGbpAmount(), "Late registration fee"));
            //Add to totals
            kesTotal = kesTotal.add(lateFee.getKesAmount());
            usdTotal = usdTotal.add(lateFee.getUsdAmount());
            gbpTotal = gbpTotal.add(lateFee.getGbpAmount());
        }
        invoice.setKesTotal(kesTotal);
        invoice.setUsdTotal(usdTotal);
        invoice.setGbpTotal(gbpTotal);
        invoice.setFeeCode(new FeeCode(REGISTRATION_FEE));
        return invoice;
    }

    /**
     *
     * @param studentCourse
     * @return
     * @throws CustomHttpException
     */
    public Invoice generateRenewalInvoice(StudentCourse studentCourse) throws CustomHttpException {
        BigDecimal kesTotal = new BigDecimal(0), usdTotal = new BigDecimal(0), gbpTotal = new BigDecimal(0);
        //Generate invoice
        Invoice invoice = new Invoice(UUID.randomUUID().toString(), new Date());
        Fee renewalFee = feeTypeFacade.getAnnualRegistrationRenewalFee(studentCourse.getCourse());
        invoice.addInvoiceDetail(new InvoiceDetail(renewalFee.getKesAmount(), renewalFee.getUsdAmount(), renewalFee.getGbpAmount(), "Annual registration renewal fee"));
        Fee adminFee = feeTypeFacade.getAdministrativeFee();
        invoice.addInvoiceDetail(new InvoiceDetail(adminFee.getKesAmount(), adminFee.getUsdAmount(), adminFee.getGbpAmount(), "Administrative fee"));
        invoice.setStudentCourse(studentCourse);
        //Add to totals
        kesTotal = kesTotal.add(renewalFee.getKesAmount());
        usdTotal = usdTotal.add(renewalFee.getUsdAmount());
        gbpTotal = gbpTotal.add(renewalFee.getGbpAmount());

        invoice.setKesTotal(kesTotal);
        invoice.setUsdTotal(usdTotal);
        invoice.setGbpTotal(gbpTotal);
        invoice.setFeeCode(new FeeCode(REGISTRATION_RENEWAL_FEE));
        return invoice;
    }

    public ExemptionInvoice generateExemptionInvoice(StudentCourse studentCourse) throws CustomHttpException {
        //Mark unpaid exemption invoices as null
        Iterator<Invoice> iter = studentCourse.getInvoices().iterator();
        while (iter.hasNext()) {
            Invoice existing = iter.next();
            if (existing.getFeeCode().getCode().equals(EXEMPTION_FEE) && existing.getStatus().getStatus().equals("PENDING")) {
                iter.remove();
                existing.setStatus(new InvoiceStatus("CANCELLED"));
            }
        }
        BigDecimal kesTotal = new BigDecimal(0), usdTotal = new BigDecimal(0), gbpTotal = new BigDecimal(0);
        //Generate invoice
        ExemptionInvoice invoice = new ExemptionInvoice(studentCourse.getExemptions(), UUID.randomUUID().toString(), new Date());
        for (Paper paper : studentCourse.getExemptedPapers()) {
            StudentCourseExemptionPaperPK pk = new StudentCourseExemptionPaperPK(studentCourse.getId(), paper.getCode());
            StudentCourseExemptionPaper studentCourseExemptionPaper = new StudentCourseExemptionPaper(pk);
            Fee exemptionFee = feeTypeFacade.getExemptionFee(paper);
            invoice.addExemptionInvoiceDetail(new ExemptionInvoiceDetail(studentCourseExemptionPaper, exemptionFee.getKesAmount(), exemptionFee.getUsdAmount(), exemptionFee.getGbpAmount(), "Exemption fee | " + paper.getCode()));
            //Add to totals
            kesTotal = kesTotal.add(exemptionFee.getKesAmount());
            usdTotal = usdTotal.add(exemptionFee.getUsdAmount());
            gbpTotal = gbpTotal.add(exemptionFee.getGbpAmount());
        }
        Fee adminFee = feeTypeFacade.getAdministrativeFee();
        invoice.addInvoiceDetail(new InvoiceDetail(adminFee.getKesAmount(), adminFee.getUsdAmount(), adminFee.getGbpAmount(), "Administrative fee"));
        invoice.setStudentCourse(studentCourse);
        invoice.setKesTotal(kesTotal);
        invoice.setUsdTotal(usdTotal);
        invoice.setGbpTotal(gbpTotal);
        invoice.setFeeCode(new FeeCode(EXEMPTION_FEE));
        return invoice;
    }

    public Invoice generateExamEntryInvoice(StudentCourseSitting entity, Map<String, Collection<Paper>> map) throws CustomHttpException {
        StudentCourseSitting managed = em.find(StudentCourseSitting.class, entity.getId());
        BigDecimal kesTotal = new BigDecimal(0), usdTotal = new BigDecimal(0), gbpTotal = new BigDecimal(0);
        //Generate invoice
        Invoice invoice = new Invoice(UUID.randomUUID().toString(), new Date());
        for (Map.Entry<String, Collection<Paper>> entry : map.entrySet()) {
            String key = entry.getKey();
            Collection<Paper> papersCollection = entry.getValue();
            List<Paper> papers = new ArrayList<>();
            papers.addAll(papersCollection);
            switch (key) {
                case "PER_PART": {
                    Part part = papers.get(0).getPart();
                    Fee fee = feeTypeFacade.getExamEntryFeePerPart(part);
                    kesTotal = fee.getKesAmount();
                    usdTotal = fee.getUsdAmount();
                    gbpTotal = fee.getGbpAmount();
                    invoice.addInvoiceDetail(new InvoiceDetail(kesTotal, usdTotal, gbpTotal, "Examination Fee | " + part.getName()));
                }
                break;
                case "PER_SECTION": {
                    Section section = papers.get(0).getSection();
                    Fee fee = feeTypeFacade.getExamEntryFeePerSection(section);
                    kesTotal = fee.getKesAmount();
                    usdTotal = fee.getUsdAmount();
                    gbpTotal = fee.getGbpAmount();
                    invoice.addInvoiceDetail(new InvoiceDetail(kesTotal, usdTotal, gbpTotal, "Examination Fee | " + section.getName()));
                }
                break;
                case "PER_LEVEL": {
                    Level level = papers.get(0).getLevel();
                    Fee fee = feeTypeFacade.getExamEntryFeePerLevel(level);
                    kesTotal = fee.getKesAmount();
                    usdTotal = fee.getUsdAmount();
                    gbpTotal = fee.getGbpAmount();
                    invoice.addInvoiceDetail(new InvoiceDetail(kesTotal, usdTotal, gbpTotal, "Examination Fee | " + level.getName()));
                }
                break;
                case "PER_PAPER": {
                    for (Paper paper : papers) {
                        Fee fee = feeTypeFacade.getExamEntryFeePerPaper(paper);
                        kesTotal = kesTotal.add(fee.getKesAmount());
                        usdTotal = usdTotal.add(fee.getUsdAmount());
                        gbpTotal = gbpTotal.add(fee.getGbpAmount());
                        invoice.addInvoiceDetail(new InvoiceDetail(fee.getKesAmount(), fee.getUsdAmount(), fee.getGbpAmount(), "Examination Fee | " + paper.getCode()));
                    }
                }
                break;
            }
        }
        Fee adminFee = feeTypeFacade.getAdministrativeFee();
        invoice.addInvoiceDetail(new InvoiceDetail(adminFee.getKesAmount(), adminFee.getUsdAmount(), adminFee.getGbpAmount(), "Administrative fee"));
        //Set totals
        invoice.setKesTotal(kesTotal);
        invoice.setUsdTotal(usdTotal);
        invoice.setGbpTotal(gbpTotal);
        invoice.setFeeCode(new FeeCode(FEE_CODE));
        invoice.setStudentCourse(managed.getStudentCourse());
        return invoice;
    }

}
