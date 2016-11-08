/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kasneb.client.Course;
import com.kasneb.client.CpaRegistration;
import com.kasneb.client.CsQualification;
import com.kasneb.client.CsSex;
import com.kasneb.client.LearnAbout;
import com.kasneb.client.Nation;
import com.kasneb.client.Receipt;
import com.kasneb.client.ReceiptCategory;
import com.kasneb.client.ReceiptDetail;
import com.kasneb.client.ReceiptDetailPK;
import com.kasneb.client.Stream;
import com.kasneb.entity.Country;
import com.kasneb.entity.Currency;
import com.kasneb.entity.ExamCentre;
import com.kasneb.entity.Fee;
import com.kasneb.entity.FeeCode;
import com.kasneb.entity.FeeTypeCode;
import com.kasneb.entity.Invoice;
import com.kasneb.entity.InvoiceDetail;
import com.kasneb.entity.KasnebCourse;
import com.kasneb.entity.Paper;
import com.kasneb.entity.Section;
import com.kasneb.entity.Sitting;
import com.kasneb.entity.SittingPeriod;
import com.kasneb.entity.Student;
import com.kasneb.entity.StudentCourse;
import com.kasneb.exception.CustomHttpException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author jikara
 */
public class CoreUtil {

    public static String BASE_URL = "http://localhost:29097/core/";

    public static List<Country> getCountries() throws IOException, CustomHttpException {
        Gson gson = new Gson();
        return gson.fromJson(new RestUtil().doGet(BASE_URL + "api/nation"), new TypeToken<List<Country>>() {
        }.getType());
    }

    public static List<KasnebCourse> getCourses() throws IOException, CustomHttpException {
        Gson gson = new Gson();
        return gson.fromJson(new RestUtil().doGet(BASE_URL + "api/course"), new TypeToken<List<KasnebCourse>>() {
        }.getType());
    }

    public static Fee getRegistrationFee(KasnebCourse course) throws IOException, CustomHttpException {
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        //Create core object
        String kesResponse = new RestUtil().doGet(BASE_URL + "api/csregfee/" + course.getId() + "/" + Currency.KSH.toString());
        String usdResponse = new RestUtil().doGet(BASE_URL + "api/csregfee/" + course.getId() + "/" + Currency.USD.toString());
        com.kasneb.client.RegistrationFee kesRegistrationFee = gson.fromJson(kesResponse, com.kasneb.client.RegistrationFee.class);
        com.kasneb.client.RegistrationFee usdRegistrationFee = gson.fromJson(usdResponse, com.kasneb.client.RegistrationFee.class);
        return new Fee(null, "Professional exam registration fee", new BigDecimal(0), kesRegistrationFee.getRegistrationFee(), usdRegistrationFee.getRegistrationFee(), kesRegistrationFee.getLastEdited(), new FeeTypeCode("course_registration_fees"), new FeeCode("REGISTRATION_FEE"), new KasnebCourse(kesRegistrationFee.getCourse().getId()), null, null, null, null, null);
    }

    public static Fee getExemptionFee(Paper entity) throws IOException, CustomHttpException {
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        //Create core object 
        String response = new RestUtil().doGet(BASE_URL + "api/cpa/paper/" + entity.getCode());
        com.kasneb.client.Paper paper = gson.fromJson(response, com.kasneb.client.Paper.class);
        return new Fee(null, "Exemption Fee", new BigDecimal(0), paper.getExemptionFee(), new BigDecimal(0), new Date(), new FeeTypeCode("exemption_fee_per_paper"), new FeeCode("EXEMPTION_FEE"), new KasnebCourse("01"), null, null, null, null, null);
    }

    public static List<ExamCentre> getCentres() throws IOException, CustomHttpException {
        Gson gson = new Gson();
        return gson.fromJson(new RestUtil().doGet(BASE_URL + "api/centre"), new TypeToken<List<ExamCentre>>() {
        }.getType());
    }

    public static Fee getCpaExaminationFee(Section section, String rate) throws IOException, CustomHttpException {
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        //Create core object 
        String kesResponse = new RestUtil().doGet(BASE_URL + "api/cpa/fee/" + section.getId() + "/" + Currency.KSH.toString());
        String usdResponse = new RestUtil().doGet(BASE_URL + "api/cpa/fee/" + section.getId() + "/" + Currency.USD.toString());
        com.kasneb.client.CpaExaminationFee kesExaminationFee = gson.fromJson(kesResponse, com.kasneb.client.CpaExaminationFee.class);
        com.kasneb.client.CpaExaminationFee usdExaminationFee = gson.fromJson(usdResponse, com.kasneb.client.CpaExaminationFee.class);
        switch (rate) {
            case "paper":
                return new Fee(null, "Professional exam registration fee", new BigDecimal(0), kesExaminationFee.getPaperFee(), usdExaminationFee.getSectionFee(), new Date(), new FeeTypeCode("exam_entry_fee_per_paper"), new FeeCode("EXAM_ENTRY_FEE"), section.getPart().getCourse(), null, null, null, null, null);
            case "section":
                return new Fee(null, "Professional exam registration fee", new BigDecimal(0), kesExaminationFee.getPaperFee(), usdExaminationFee.getSectionFee(), new Date(), new FeeTypeCode("exam_entry_fee_per_section"), new FeeCode("EXAM_ENTRY_FEE"), section.getPart().getCourse(), null, null, null, null, null);
            case "part":
                return new Fee(null, "Professional exam registration fee", new BigDecimal(0), kesExaminationFee.getPaperFee().multiply(new BigDecimal(2)), usdExaminationFee.getSectionFee().multiply(new BigDecimal(2)), new Date(), new FeeTypeCode("exam_entry_fee_per_part"), new FeeCode("EXAM_ENTRY_FEE"), section.getPart().getCourse(), null, null, null, null, null);
        }
        return null;
    }

    public static Fee getCpsExaminationFee(Section section, String rate) throws IOException, CustomHttpException {
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        //Create core object 
        String kesResponse = new RestUtil().doGet(BASE_URL + "api/cps/fee/" + section.getId() + "/" + Currency.KSH.toString());
        String usdResponse = new RestUtil().doGet(BASE_URL + "api/cps/fee/" + section.getId() + "/" + Currency.USD.toString());
        com.kasneb.client.CpaExaminationFee kesExaminationFee = gson.fromJson(kesResponse, com.kasneb.client.CpaExaminationFee.class);
        com.kasneb.client.CpaExaminationFee usdExaminationFee = gson.fromJson(usdResponse, com.kasneb.client.CpaExaminationFee.class);
        switch (rate) {
            case "paper":
                return new Fee(null, "CS exam entry fee", new BigDecimal(0), kesExaminationFee.getPaperFee(), usdExaminationFee.getSectionFee(), new Date(), new FeeTypeCode("exam_entry_fee_per_paper"), new FeeCode("EXAM_ENTRY_FEE"), section.getPart().getCourse(), null, null, null, null, null);
            case "section":
                return new Fee(null, "CS exam entry fee", new BigDecimal(0), kesExaminationFee.getPaperFee(), usdExaminationFee.getSectionFee(), new Date(), new FeeTypeCode("exam_entry_fee_per_section"), new FeeCode("EXAM_ENTRY_FEE"), section.getPart().getCourse(), null, null, null, null, null);
            case "part":
                return new Fee(null, "CS exam entry fee", new BigDecimal(0), kesExaminationFee.getPaperFee().multiply(new BigDecimal(2)), usdExaminationFee.getSectionFee().multiply(new BigDecimal(2)), new Date(), new FeeTypeCode("exam_entry_fee_per_part"), new FeeCode("EXAM_ENTRY_FEE"), section.getPart().getCourse(), null, null, null, null, null);
        }
        return null;
    }

    public static CpaRegistration getStudentCourse(Integer regNo) throws IOException, CustomHttpException {
        Gson gson = new Gson();
        return gson.fromJson(new RestUtil().doGet(BASE_URL + "api/cpa/" + regNo), CpaRegistration.class);
    }

    public static CpaRegistration registerStudent(StudentCourse studentCourse) throws IOException, CustomHttpException {
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        Student student = studentCourse.getStudent();
        //Create core object
        Set<Receipt> receipts = new HashSet<>();
        CpaRegistration registration = new CpaRegistration(null, Stream.AC, getFirstExemDate(studentCourse.getFirstSitting()), student.getLastName(), studentCourse.getStudent().getFirstName(), student.getMiddleName(), "", new CsSex("M"), student.getDob(), new Nation(student.getCountryId().getCode()), student.getDocumentNo(), new CsQualification(1), null, "C1135308", "", "", student.getContact().getPostalAddress(), "", student.getContact().getPostalAddress(), student.getContact().getTown(), student.getContact().getCountryId().getName(), student.getEmail(), student.getPhoneNumber(), "", new Course("00"), "Media", new LearnAbout(4), new Nation("1"), new CsQualification(5));
        for (Invoice invoice : studentCourse.getInvoices()) {
            if ("PAID".equals(invoice.getStatus().getStatus())) {
                Collection<ReceiptDetail> receiptDetails = new ArrayList<>();
                for (InvoiceDetail invDetail : invoice.getInvoiceDetails()) {
                    ReceiptDetailPK receiptDetailPK = new ReceiptDetailPK(null, studentCourse.getCourse().getId(), invoice.getFeeCode().getCode());
                    ReceiptDetail rcpDetail = new ReceiptDetail(receiptDetailPK, null, new Course(studentCourse.getCourse().getId()), new ReceiptCategory("REG"), invDetail.getDescription(), invDetail.getKesAmount(), registration, "", new com.kasneb.client.Currency(Currency.KSH.toString()));
                    receiptDetails.add(rcpDetail);
                }
                Receipt receipt = new Receipt(generateReceipt(), new Course(studentCourse.getCourse().getId()), studentCourse.getStudent().getFirstName(), registration, new Date(), "999", invoice.getKesTotal(), "wrwerr", new com.kasneb.client.Currency(Currency.KSH.toString()), receiptDetails);
                receipts.add(receipt);
            }
        }
        registration.setReceipts(receipts);
        String jsonReq = mapper.writeValueAsString(registration);
        String jsonResponse = new RestUtil().doPost(BASE_URL + "api/cpa", jsonReq);
        CpaRegistration created = gson.fromJson(jsonResponse, CpaRegistration.class);
        return created;
    }

    public static Integer getFirstExemDate(Sitting firstSitting) {
        Integer session;
        if (firstSitting.getSittingPeriod().equals(SittingPeriod.MAY)) {
            session = 1;
        } else {
            session = 2;
        }
        return Integer.parseInt(session + "" + firstSitting.getSittingYear());
    }

    public static String generateReceipt() {
        Integer maximum = 10000;
        Integer minimum = 1000;
        Random rn = new Random();
        int n = maximum - minimum + 1;
        int i = rn.nextInt() % n;
        return "C" + minimum + i;
    }

    public static Integer generateRegistrationNumber() {
        return 1;
    }
}
