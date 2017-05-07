/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.client.Centre;
import com.kasneb.client.ExamEntry;
import com.kasneb.client.ExamPaper;
import com.kasneb.entity.Contact;
import com.kasneb.entity.Exemption;
import com.kasneb.entity.ExemptionPaper;
import com.kasneb.entity.Invoice;
import com.kasneb.entity.InvoiceDetail;
import com.kasneb.entity.Level;
import com.kasneb.entity.Part;
import com.kasneb.entity.Payment;
import com.kasneb.entity.Section;
import com.kasneb.entity.Student;
import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.StudentCourseSitting;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.jasper.ExemptionDocument;
import com.kasneb.jasper.ExemptionDocument.ExemptionLevel;
import com.kasneb.jasper.ExemptionDocument.ExemptionPart;
import com.kasneb.jasper.ExemptionDocument.ExemptionPart.ExemptionSection;
import com.kasneb.jasper.ReceiptDocument;
import com.kasneb.jasper.TimetableDocument;
import com.kasneb.jasper.TimetableDocument.Paper;
import com.kasneb.util.Constants;
import com.kasneb.util.CoreUtil;
import com.kasneb.util.DateUtil;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import org.joda.time.DateTime;

/**
 *
 * @author jikara
 */
@Stateless
public class ExportFacade {

    @EJB
    com.kasneb.session.InvoiceFacade invoiceFacade;
    @EJB
    com.kasneb.session.StudentCourseFacade studentCourseFacade;
    @EJB
    com.kasneb.session.StudentCourseSittingFacade studentCourseSittingFacade;
    @EJB
    com.kasneb.session.PaymentFacade paymentFacade;
    @EJB
    com.kasneb.session.PaperFacade paperFacade;
    @EJB
    com.kasneb.session.ExemptionFacade exemptionFacade;

    public com.kasneb.jasper.ReceiptDocument generateReceipt(Integer transactionId) throws IOException, CustomHttpException, ParseException {
        Payment payment = paymentFacade.find(transactionId);
        StudentCourse studentCourse = payment.getStudentCourse();
        ReceiptDocument doc = null;
        if (payment.getPaymentDetails() != null) {
            List<InvoiceDetail> items = payment.getInvoice().getInvoiceDetails();
            String fullName = studentCourse.getStudent().getFullName();
            doc = new com.kasneb.jasper.ReceiptDocument(payment.getTotalAmount(), payment.getKasnebRef(), fullName, studentCourse.getStudent().getContact().getPostalAddress(), studentCourse.getStudent().getContact().getTown(), studentCourse.getStudent().getCountryId().getName(), payment.getReceiptNo(), DateUtil.getString(payment.getPaymentTimestamp()), payment.getStudentCourse().getFullRegistrationNumber(), payment.getStudentCourse().getCourse().getName(), items);
            if (payment.getInvoice() != null) {
                switch (payment.getInvoice().getFeeCode().getCode()) {
                    case Constants.EXAM_ENTRY_FEE:
                        StudentCourseSitting sitting = payment.getInvoice().getStudentCourseSitting();
                        doc.setType(Constants.EXAM_ENTRY_FEE);
                        doc.setCentre(sitting.getSittingCentre().getName());
                        break;
                    case Constants.REGISTRATION_FEE:
                        doc.setType(Constants.REGISTRATION_FEE);
                        doc.setFirstExamDate(studentCourse.getFirstSitting().getSittingDescription());
                        DateTime renewalDate = new DateTime(studentCourse.getLastSubscription().getExpiry());
                        renewalDate = renewalDate.plusDays(1);
                        doc.setFirstRenewalDate(DateUtil.getString(renewalDate.toDate()));
                        break;
                }
            }
        }
        return doc;
    }

    public com.kasneb.jasper.TimetableDocument generateTimetable(Integer studentCourseSittingId) throws IOException, CustomHttpException, ParseException {
        StudentCourseSitting studentCourseSitting = studentCourseSittingFacade.find(studentCourseSittingId);
        StudentCourse studentCourse = studentCourseSitting.getStudentCourse();
        Student student = studentCourse.getStudent();
        String fullRegistrationNumber = studentCourse.getFullRegistrationNumber();
        String nameAndId = student.getFullName() + " " + student.getDocumentNo();
        Integer period = 0;
        switch (studentCourseSitting.getSitting().getSittingPeriod()) {
            case MAY:
                period = 1;
                break;
            case NOVEMBER:
                period = 2;
                break;
        }
        ExamEntry examEntry = CoreUtil.getExam(studentCourse.getCourse(), studentCourse.getRegistrationNumber(), studentCourseSitting.getSitting().getSittingYear(), period);
        Centre centre = examEntry.getCentre();
        String centreName = null;
        if (centre != null) {
            centreName = centre.getName();
        }
        TimetableDocument timetable = new TimetableDocument(fullRegistrationNumber, nameAndId, DateUtil.getString(new Date()), studentCourseSitting.getSitting().getSittingDescription(), centreName, student.getFullName(), student.getContact().getPostalAddress(), student.getContact().getTown(), student.getContact().getCountryId().getName());
        List<Paper> papers = new ArrayList<>();
        for (ExamPaper examPaper : examEntry.getExamPapers()) {
            com.kasneb.client.Paper paper = examPaper.getPaper();
            com.kasneb.entity.Paper dbPaper = paperFacade.find(paper.getCode());
            String dayPart = "9.00 A.M. - 12.00 NOON";
            String examDetails;
            if (paper.getPart() != null) {
                examDetails = studentCourse.getCourse().getName() + " " + dbPaper.getSection().getPart().getName();
            } else {
                examDetails = studentCourse.getCourse().getName() + " " + dbPaper.getLevel().getName();
            }
            timetable.setExaminationDetails(examDetails);
            switch (paper.getDayPart()) {
                case 1:
                    dayPart = "9.00 A.M. - 12.00 NOON";
                    break;
                case 2:
                    dayPart = "2.00 P.M. - 5.00 P.M.";
                    break;
            }
            String paperDate = DateUtil.convertFormat("dd-MM-yyyy", "EEEE dd/MM/yyyy", paper.getExamDate());
            Paper timetablePaper = timetable.new Paper(paper.getCode(), paper.getName(), paperDate, dayPart);
            papers.add(timetablePaper);
        }
        timetable.setPapers(papers);
        return timetable;
    }

    public com.kasneb.jasper.ExemptionDocument generateExemptionLetter(Exemption exemption) throws IOException, CustomHttpException, ParseException {
        StudentCourse studentCourse = exemption.getStudentCourse();
        Student student = studentCourse.getStudent();
        Contact contact = student.getContact();
        String reference = exemption.getReference();
        Set<Section> sections = new HashSet<>();
        Set<Part> parts = new HashSet<>();
        Set<Level> levels = new HashSet<>();
        if (exemption.getPapers().isEmpty()) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Exemptions cannot be enmpty");
        }
        for (ExemptionPaper exemptionPaper : exemption.getPapers()) {
            switch (studentCourse.getCourse().getCourseTypeCode()) {
                case 100:
                    parts.add(exemptionPaper.getPaper().getSection().getPart());
                    sections.add(exemptionPaper.getPaper().getSection());
                    break;
                case 200:
                    levels.add(exemptionPaper.getPaper().getLevel());
                    break;
                case 300:
                    sections.add(exemptionPaper.getPaper().getSection());
                    break;
            }
        }
        ExemptionDocument document = new ExemptionDocument(studentCourse.getCourse().getName(), reference, DateUtil.getString(exemption.getDateVerified()), studentCourse.getFullRegistrationNumber(),student.getFullName(), contact.getPostalAddress() + " - " + contact.getPostalCode(), contact.getTown(), contact.getCountryId().getName(), "Test");
        Set<ExemptionPart> exemptionParts = new HashSet<>();
        Set<ExemptionSection> exemptionSections = new HashSet<>();
        Set<ExemptionLevel> exemptionLevels = new HashSet<>();
        switch (studentCourse.getCourse().getCourseTypeCode()) {
            case 100:
                for (Part part : parts) {
                    ExemptionPart exemptionPart = document.new ExemptionPart(part.getPartPK().getId(), part, studentCourse.getCourse().getName() + "    " + part.getName(), new ArrayList<>());
                    for (Section section : sections) {
                        ExemptionSection sec = exemptionPart.new ExemptionSection(part, section, new ArrayList<>());
                        for (ExemptionPaper exemptionPaper : exemption.getPapers()) {
                            if (exemptionPaper.getPaper().getSection().equals(section)) {
                                sec.addPaper(document.new ExemptionPaper(exemptionPaper.getPaper()));
                            }
                        }
                        exemptionPart.addExemptionSection(sec);
                    }
                    exemptionParts.add(exemptionPart);
                }
                document.setParts(exemptionParts);
                break;

            case 200:
                for (Level level : levels) {
                    List<com.kasneb.jasper.ExemptionDocument.ExemptionPaper> levelExemptions = new ArrayList<>();
                    for (ExemptionPaper exemptionPaper : exemption.getPapers()) {
                        if (exemptionPaper.getPaper().getLevel().getLevelPK().getId().equals(level.getLevelPK().getId())) {
                            levelExemptions.add(document.new ExemptionPaper(exemptionPaper.getPaper()));
                        }
                    }
                    exemptionLevels.add(document.new ExemptionLevel(level.getLevelPK().getId(), studentCourse.getCourse().getName() + "    " + level.getName(), levelExemptions));
                }
                document.setLevels(exemptionLevels);
                break;
        }
        return document;
    }

    public com.kasneb.jasper.InvoiceDocument generateInvoice(Integer invoiceId) throws ParseException, CustomHttpException {
        Invoice invoice = invoiceFacade.find(invoiceId);
        if (invoice == null) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invoice does not exist");
        }
        String courseName = invoice.getStudentCourse().getCourse().getName();
        String fullName = invoice.getStudentCourse().getStudent().getFullName();
        String fullRegistrationNumber = invoice.getStudentCourse().getFullRegistrationNumber();
        String address = invoice.getStudentCourse().getStudent().getContact().getPostalAddress();
        String referenceNumber = invoice.getReference();
        String town = invoice.getStudentCourse().getStudent().getContact().getTown();
        String country = invoice.getStudentCourse().getStudent().getContact().getCountryId().getName();
        String invoiceNo = invoice.getReference();
        String Date = DateUtil.getString(invoice.getDateGenerated(), "dd/MM/yyyy");
        com.kasneb.jasper.InvoiceDocument doc = new com.kasneb.jasper.InvoiceDocument(courseName, fullName, fullRegistrationNumber, address, referenceNumber, town, country, invoiceNo, Date, invoice.getLocalAmount(), invoice.getLocalCurrency().toString(), DateUtil.getString(invoice.getDueDate(), "dd/MM/yyyy"));
        List<InvoiceDetail> items = invoice.getInvoiceDetails();
        doc.setItems(items);
        return doc;
    }
}
