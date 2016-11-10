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
import com.kasneb.client.Registration;
import com.kasneb.client.Qualification;
import com.kasneb.client.Sex;
import com.kasneb.client.LearnAbout;
import com.kasneb.client.Nation;
import com.kasneb.client.Receipt;
import com.kasneb.client.ReceiptCategory;
import com.kasneb.client.ReceiptDetail;
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
        com.kasneb.client.ExaminationFee kesExaminationFee = gson.fromJson(kesResponse, com.kasneb.client.ExaminationFee.class);
        com.kasneb.client.ExaminationFee usdExaminationFee = gson.fromJson(usdResponse, com.kasneb.client.ExaminationFee.class);
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
        com.kasneb.client.ExaminationFee kesExaminationFee = gson.fromJson(kesResponse, com.kasneb.client.ExaminationFee.class);
        com.kasneb.client.ExaminationFee usdExaminationFee = gson.fromJson(usdResponse, com.kasneb.client.ExaminationFee.class);
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

    public static Registration getStudentCourse(Integer regNo) throws IOException, CustomHttpException {
        Gson gson = new Gson();
        String responseJson = new RestUtil().doGet(BASE_URL + "api/cpa/" + regNo);
        return gson.fromJson(responseJson, Registration.class);
    }

    public static Registration registerStudent(StudentCourse studentCourse) throws IOException, CustomHttpException {
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        Student student = studentCourse.getStudent();
        //Create core object
        Set<Receipt> receipts = new HashSet<>();
        //String regNo, String registrationNumber, Stream stream, String stringStream, Date registered, Integer firstExamDate,
        //String lastName, String firstName, String otherName, String otherName2, Sex sex, Date dateOfBirth, String idNumber,
        //Qualification quali, Date rrDate, String rrNumber, String pReg, String idNo2, String address1, String address2,
        //String address3, String address4, String address5, String email, String cellphone, String telephone,
        //Course previousCourse, String learnAbout, LearnAbout learnt, Nation nationality, Qualification qualification,
        //List<Receipt> receipts, List<StudentCoursePaper> eligiblePapers, List<Exemption> exemptions, List<ExamBooking> examBookings, ExamEntry cpaExamEntry
        Registration registration = new Registration(null, //regNo
                "",//registrationNumber
                Stream.AC, //stream
                "", //stringStream
                student.getCreated(), //registered
                getFirstExemDate(studentCourse.getFirstSitting()), //firstExamDate
                student.getLastName(), //lastName
                studentCourse.getStudent().getFirstName(), //firstName
                student.getMiddleName(), //otherName
                "", //otherName2
                new Sex("M"), //Sex 
                student.getDateOfBirth().toString(), //dateOfBirth
                student.getDocumentNo(),//idNumber
                new Qualification(1), //quali
                new Date(), //rrDAre
                "", //rrNumber
                student.getPreviousRegistrationNo() + "", //pReg
                "", //idNo2
                student.getContact().getPostalAddress(), //address1
                "",//address2
                "",//address3
                "",//address4
                "",//address5
                student.getEmail(), //email
                student.getPhoneNumber(), //Cell phone
                "", //Telephone
                new Course(student.getPreviousCourseCode()), //previousCourse
                "", //Learn about
                new LearnAbout(4), //learnt 
                new Nation(student.getCountryId().getCode()), //Nationality
                new Qualification(5),
                null,//receipts
                null,//eligiblePapers
                null,//exemptions
                null,//examBookings
                null//cpaExamEntry
        );
        for (Invoice invoice : studentCourse.getInvoices()) {
            if ("PAID".equals(invoice.getStatus().getStatus())) {
                Collection<ReceiptDetail> receiptDetails = new ArrayList<>();
                //String receiptNo, Course course, String receivedFrom, Registration registration, String fullRegistrationNumber, String lastUser, Date mdate, String paymentType, BigDecimal amount, String referenceNumber, Currency currency, BigDecimal amount2, List<ReceiptDetail> receiptDetails
                Receipt receipt = new Receipt(generateReceipt(), new Course(studentCourse.getCourse().getId()), studentCourse.getStudent().getFirstName(), registration, "", "MOBILE", new Date(), "999", invoice.getKesTotal(), "wrwerr", new com.kasneb.client.Currency(Currency.KSH.toString()), new BigDecimal(0), receiptDetails);
                for (InvoiceDetail invDetail : invoice.getInvoiceDetails()) {
                    //Receipt receipt, Course course, String lastUser, Date created, String studentName, ReceiptCategory category, String description, BigDecimal amount, Registration registration, String postingCode, String fullRegNo, Currency currency
                    String studentName = studentCourse.getStudent().getFirstName();
                    ReceiptDetail rcpDetail = new ReceiptDetail(receipt, new Course(studentCourse.getCourse().getId()), "MOBILE", new Date(), studentName, new ReceiptCategory("REG"), invDetail.getDescription(), invDetail.getKesAmount(), registration, "", "", new com.kasneb.client.Currency(Currency.KSH.toString()));
                    receiptDetails.add(rcpDetail);
                }
                receipts.add(receipt);
            }
        }
        registration.setReceipts(receipts);
        String jsonReq = mapper.writeValueAsString(registration);
        String jsonResponse = new RestUtil().doPost(BASE_URL + "api/cpa", jsonReq);
        Registration created = gson.fromJson(jsonResponse, Registration.class);
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
