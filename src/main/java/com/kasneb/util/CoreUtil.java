/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kasneb.client.CoreExemption;
import com.kasneb.client.ExamEntry;
import com.kasneb.client.Exemption;
import com.kasneb.client.Registration;
import com.kasneb.client.Receipt;
import com.kasneb.client.Renewal;
import com.kasneb.client.StudentCoursePaper;
import com.kasneb.entity.CentreZone;
import com.kasneb.entity.Country;
import com.kasneb.entity.Currency;
import com.kasneb.entity.ExamCentre;
import com.kasneb.entity.Fee;
import com.kasneb.entity.FeeCode;
import com.kasneb.entity.FeeTypeCode;
import com.kasneb.entity.KasnebCourse;
import com.kasneb.entity.KasnebCourseType;
import com.kasneb.entity.Level;
import com.kasneb.entity.Paper;
import com.kasneb.entity.Section;
import com.kasneb.entity.Sitting;
import com.kasneb.entity.SittingPeriod;
import com.kasneb.entity.StudentCourse;
import com.kasneb.exception.CustomHttpException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jikara
 */
public class CoreUtil {

    public static List<Country> getCountries() throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        String json = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/nation");
        return gson.fromJson(json, new TypeToken<List<Country>>() {
        }.getType());
    }

    public static List<KasnebCourseType> getCourseTypes() throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        String json = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/coursetype");
        // Logger.getLogger("getCourseTypes").log(java.util.logging.Level.INFO, null, json);
        return gson.fromJson(json, new TypeToken<List<KasnebCourseType>>() {
        }.getType());
    }

    public static List<KasnebCourse> getCourses() throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        String json = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/course");
        // Logger.getLogger("getCourses").log(java.util.logging.Level.INFO, null, json);
        return gson.fromJson(json, new TypeToken<List<KasnebCourse>>() {
        }.getType());
    }

    public static List<KasnebCourse> getCourses(Integer courseTypeCode) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        String json = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/course/type/" + courseTypeCode);
        // Logger.getLogger("getCourses").log(java.util.logging.Level.INFO, null, json);
        return gson.fromJson(json, new TypeToken<List<KasnebCourse>>() {
        }.getType());
    }

    public static List<Exemption> getExemptions(KasnebCourse course, Integer regNo) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        String json = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/exemption?courseCode=" + course.getId() + "&regNo=" + regNo);
        // Logger.getLogger("getExemptions").log(java.util.logging.Level.INFO, null, json);
        return gson.fromJson(json, new TypeToken<List<Exemption>>() {
        }.getType());
    }

    public static ExamEntry getExam(KasnebCourse course, Integer regNo, Integer year, Integer period) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        String responseJson = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/exam/" + course.getId() + "?regNo=" + regNo + "&year=" + year + "&period=" + period);
        return gson.fromJson(responseJson, ExamEntry.class);
    }

    public static Receipt getReceipt(String receiptNumber) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        String responseJson = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/receipt/" + receiptNumber);
        return gson.fromJson(responseJson, Receipt.class);
    }

    public static Fee getRegistrationFee(KasnebCourse course) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        ObjectMapper mapper = new ObjectMapper();
        //Create core object
        String kesResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/registration/" + course.getId() + "?currency=" + Currency.KSH.toString());
        String usdResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/registration/" + course.getId() + "?currency=" + Currency.USD.toString());
        com.kasneb.client.RegistrationFee kesRegistrationFee = gson.fromJson(kesResponse, com.kasneb.client.RegistrationFee.class);
        com.kasneb.client.RegistrationFee usdRegistrationFee = gson.fromJson(usdResponse, com.kasneb.client.RegistrationFee.class);
        return new Fee(null, "Exam registration fee", new BigDecimal(0), kesRegistrationFee.getRegistrationFee(), usdRegistrationFee.getRegistrationFee(), null, new FeeTypeCode("course_registration_fees"), new FeeCode("REGISTRATION_FEE"), new KasnebCourse(kesRegistrationFee.getCourse().getId()), null, null, null, null, null);
    }

    public static Fee getLateRegistrationFee(KasnebCourse course) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        ObjectMapper mapper = new ObjectMapper();
        //Create core object
        String kesResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/registration/" + course.getId() + "?currency=" + Currency.KSH.toString());
        String usdResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/registration/" + course.getId() + "?currency=" + Currency.USD.toString());
        com.kasneb.client.RegistrationFee kesRegistrationFee = gson.fromJson(kesResponse, com.kasneb.client.RegistrationFee.class);
        com.kasneb.client.RegistrationFee usdRegistrationFee = gson.fromJson(usdResponse, com.kasneb.client.RegistrationFee.class);
        return new Fee(null, "Exam registration fee", new BigDecimal(0), kesRegistrationFee.getLateRegistrationFee(), usdRegistrationFee.getLateRegistrationFee(), null, new FeeTypeCode("course_late_registration_fees"), new FeeCode("REGISTRATION_FEE"), new KasnebCourse(kesRegistrationFee.getCourse().getId()), null, null, null, null, null);
    }

    public static Fee getExemptionFee(Paper entity, KasnebCourse course) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        ObjectMapper mapper = new ObjectMapper();
        String kesResponse = null, usdResponse = null;
        //Create core object 
        switch (course.getCourseTypeCode()) {
            case 100:
                kesResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/exam/" + course.getId() + "?sectionId=" + entity.getSection().getSectionPK().getId() + "&currency=" + Currency.KSH.toString());
                usdResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/exam/" + course.getId() + "?sectionId=" + entity.getSection().getSectionPK().getId() + "&currency=" + Currency.USD.toString());
                break;
            case 200:
                kesResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/exam/" + course.getId() + "?levelId=" + entity.getLevel().getLevelPK().getId() + "&currency=" + Currency.KSH.toString());
                usdResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/exam/" + course.getId() + "?levelId=" + entity.getLevel().getLevelPK().getId() + "&currency=" + Currency.USD.toString());
                break;
        }
        com.kasneb.client.ExaminationFee kesExaminationFee = gson.fromJson(kesResponse, com.kasneb.client.ExaminationFee.class);
        com.kasneb.client.ExaminationFee usdExaminationFee = gson.fromJson(usdResponse, com.kasneb.client.ExaminationFee.class);
        return new Fee(null, "Exemption Fee", new BigDecimal(0), kesExaminationFee.getExemptionFee(), usdExaminationFee.getExemptionFee(), new Date(), new FeeTypeCode("exemption_fee_per_paper"), new FeeCode("EXEMPTION_FEE"), new KasnebCourse("01"), null, null, null, null, null);
    }

    public static Fee getRenewalFee(KasnebCourse course, Integer year) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        ObjectMapper mapper = new ObjectMapper();
        //Create core object
        String kesResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/renewal/" + course.getId() + "?currency=" + Currency.KSH.toString() + "&year=" + year);
        String usdResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/registration/" + course.getId() + "?currency=" + Currency.USD.toString());
        com.kasneb.client.RegistrationFee kesRegistrationRenewalFee = gson.fromJson(kesResponse, com.kasneb.client.RegistrationFee.class);
        com.kasneb.client.RegistrationFee usdRegistrationRenewalFee = gson.fromJson(usdResponse, com.kasneb.client.RegistrationFee.class);
        return new Fee(null, "Registration renewal fee", new BigDecimal(0), kesRegistrationRenewalFee.getRegistrationRenewalFee(), usdRegistrationRenewalFee.getRegistrationRenewalFee(), null, new FeeTypeCode("Registration_renewal_fees"), new FeeCode("REGISTRATION_RENEWAL_FEE"), new KasnebCourse(kesRegistrationRenewalFee.getCourse().getId()), null, null, null, null, null);
    }

    public static Fee getReinstatementFee(KasnebCourse course) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        ObjectMapper mapper = new ObjectMapper();
        //Create core object
        String kesResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/registration/" + course.getId() + "?currency=" + Currency.KSH.toString());
        String usdResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/registration/" + course.getId() + "?currency=" + Currency.USD.toString());
        com.kasneb.client.RegistrationFee kesReinstatementFee = gson.fromJson(kesResponse, com.kasneb.client.RegistrationFee.class);
        com.kasneb.client.RegistrationFee usdReinstatementFee = gson.fromJson(usdResponse, com.kasneb.client.RegistrationFee.class);
        return new Fee(null, "Registration reinstatement fee", new BigDecimal(0), kesReinstatementFee.getRegistrationReinstatementFee(), usdReinstatementFee.getRegistrationReinstatementFee(), null, new FeeTypeCode("registration_reinstatement_fees"), new FeeCode("REGISTRATION_REINSTATEMENT_FEE"), new KasnebCourse(kesReinstatementFee.getCourse().getId()), null, null, null, null, null);
    }

    public static List<ExamCentre> getCentres() throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        return gson.fromJson(new RestUtil().doGet(Constants.CORE_BASE_URL + "api/centre"), new TypeToken<List<ExamCentre>>() {
        }.getType());
    }

    public static List<ExamCentre> getCentres(KasnebCourse course, String zoneCode) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        return gson.fromJson(new RestUtil().doGet(Constants.CORE_BASE_URL + "api/centre?courseCode=" + course.getId() + "&zoneCode=" + zoneCode), new TypeToken<List<ExamCentre>>() {
        }.getType());
    }

    public static List<ExamCentre> getCentres(KasnebCourse course) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        return gson.fromJson(new RestUtil().doGet(Constants.CORE_BASE_URL + "api/centre?courseCode=" + course.getId()), new TypeToken<List<ExamCentre>>() {
        }.getType());
    }

    public static List<CentreZone> getZones(KasnebCourse course) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        return gson.fromJson(new RestUtil().doGet(Constants.CORE_BASE_URL + "api/zone?courseCode=" + course.getId()), new TypeToken<List<CentreZone>>() {
        }.getType());
    }

    public static List<CentreZone> getZones() throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        return gson.fromJson(new RestUtil().doGet(Constants.CORE_BASE_URL + "api/zone"), new TypeToken<List<CentreZone>>() {
        }.getType());
    }

    public static Fee getPaperExaminationFee(Paper paper, KasnebCourse course) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        ObjectMapper mapper = new ObjectMapper();
        //Create core object 
        String kesResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/exam/" + course.getId() + "?paperCode=" + paper.getCode() + "&currency=" + Currency.KSH.toString());
        String usdResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/exam/" + course.getId() + "?paperCode=" + paper.getCode() + "&currency=" + Currency.USD.toString());
        com.kasneb.client.ExaminationFee kesExaminationFee = gson.fromJson(kesResponse, com.kasneb.client.ExaminationFee.class);
        com.kasneb.client.ExaminationFee usdExaminationFee = gson.fromJson(usdResponse, com.kasneb.client.ExaminationFee.class);
        return new Fee(null, "Exam registration fee", new BigDecimal(0), kesExaminationFee.getPaperExamFee(), usdExaminationFee.getPaperExamFee(), new Date(), new FeeTypeCode("exam_entry_fee_per_section"), new FeeCode("EXAM_ENTRY_FEE"), paper.getCourse(), null, null, null, null, null);
    }

    public static Fee getPaperLateExaminationFee(Paper paper, KasnebCourse course) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        ObjectMapper mapper = new ObjectMapper();
        //Create core object 
        String kesResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/exam/" + course.getId() + "?paperCode=" + paper.getCode() + "&currency=" + Currency.KSH.toString());
        String usdResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/exam/" + course.getId() + "?paperCode=" + paper.getCode() + "&currency=" + Currency.USD.toString());
        com.kasneb.client.ExaminationFee kesExaminationFee = gson.fromJson(kesResponse, com.kasneb.client.ExaminationFee.class);
        com.kasneb.client.ExaminationFee usdExaminationFee = gson.fromJson(usdResponse, com.kasneb.client.ExaminationFee.class);
        return new Fee(null, "Exam late registration fee", new BigDecimal(0), kesExaminationFee.getPaperLateExamFee(), usdExaminationFee.getPaperLateExamFee(), new Date(), new FeeTypeCode("exam_entry_fee_per_section"), new FeeCode("EXAM_ENTRY_FEE"), paper.getCourse(), null, null, null, null, null);
    }

    public static Fee getSectionExaminationFee(Section section, KasnebCourse course) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        ObjectMapper mapper = new ObjectMapper();
        //Create core object 
        String kesResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/exam/" + course.getId() + "?sectionId=" + section.getSectionPK().getId() + "&currency=" + Currency.KSH.toString());
        String usdResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/exam/" + course.getId() + "?sectionId=" + section.getSectionPK().getId() + "&currency=" + Currency.USD.toString());
        com.kasneb.client.ExaminationFee kesExaminationFee = gson.fromJson(kesResponse, com.kasneb.client.ExaminationFee.class);
        com.kasneb.client.ExaminationFee usdExaminationFee = gson.fromJson(usdResponse, com.kasneb.client.ExaminationFee.class);
        return new Fee(null, "Exam registration fee", new BigDecimal(0), kesExaminationFee.getSectionExamFee(), usdExaminationFee.getSectionExamFee(), new Date(), new FeeTypeCode("exam_entry_fee_per_section"), new FeeCode("EXAM_ENTRY_FEE"), section.getPart().getCourse(), null, null, null, null, null);

    }

    public static Fee getSectionLateExaminationFee(Section section, KasnebCourse course) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        ObjectMapper mapper = new ObjectMapper();
        //Create core object 
        String kesResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/exam/" + course.getId() + "?sectionId=" + section.getSectionPK().getId() + "&currency=" + Currency.KSH.toString());
        String usdResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/exam/" + course.getId() + "?sectionId=" + section.getSectionPK().getId() + "&currency=" + Currency.USD.toString());
        com.kasneb.client.ExaminationFee kesExaminationFee = gson.fromJson(kesResponse, com.kasneb.client.ExaminationFee.class);
        com.kasneb.client.ExaminationFee usdExaminationFee = gson.fromJson(usdResponse, com.kasneb.client.ExaminationFee.class);
        return new Fee(null, "Exam late registration fee", new BigDecimal(0), kesExaminationFee.getSectionLateExamFee(), usdExaminationFee.getSectionLateExamFee(), new Date(), new FeeTypeCode("exam_entry_fee_per_section"), new FeeCode("EXAM_ENTRY_FEE"), section.getPart().getCourse(), null, null, null, null, null);
    }

    public static Fee getLevelExaminationFee(Level level, KasnebCourse course) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        ObjectMapper mapper = new ObjectMapper();
        //Create core object 
        String kesResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/exam/" + course.getId() + "?levelId=" + level.getLevelPK().getId() + "&currency=" + Currency.KSH.toString());
        String usdResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/exam/" + course.getId() + "?levelId=" + level.getLevelPK().getId() + "&currency=" + Currency.USD.toString());
        com.kasneb.client.ExaminationFee kesExaminationFee = gson.fromJson(kesResponse, com.kasneb.client.ExaminationFee.class);
        com.kasneb.client.ExaminationFee usdExaminationFee = gson.fromJson(usdResponse, com.kasneb.client.ExaminationFee.class);
        return new Fee(null, "Exam registration fee", new BigDecimal(0), kesExaminationFee.getSectionExamFee(), usdExaminationFee.getSectionExamFee(), new Date(), new FeeTypeCode("exam_entry_fee_per_section"), new FeeCode("EXAM_ENTRY_FEE"), level.getCourse(), null, null, null, null, null);
    }

    public static Fee getLevelLateExaminationFee(Level level, KasnebCourse course) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        ObjectMapper mapper = new ObjectMapper();
        //Create core object 
        String kesResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/exam/" + course.getId() + "?levelId=" + level.getLevelPK().getId() + "&currency=" + Currency.KSH.toString());
        String usdResponse = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/fee/exam/" + course.getId() + "?levelId=" + level.getLevelPK().getId() + "&currency=" + Currency.USD.toString());
        com.kasneb.client.ExaminationFee kesExaminationFee = gson.fromJson(kesResponse, com.kasneb.client.ExaminationFee.class);
        com.kasneb.client.ExaminationFee usdExaminationFee = gson.fromJson(usdResponse, com.kasneb.client.ExaminationFee.class);
        return new Fee(null, "Exam late registration fee", new BigDecimal(0), kesExaminationFee.getSectionLateExamFee(), usdExaminationFee.getSectionLateExamFee(), new Date(), new FeeTypeCode("exam_entry_fee_per_section"), new FeeCode("EXAM_ENTRY_FEE"), level.getCourse(), null, null, null, null, null);
    }

    public static Registration getStudentCourse(Integer regNo, KasnebCourse course) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        String responseJson = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/registration/" + course.getId() + "/" + regNo);
        return gson.fromJson(responseJson, Registration.class);
    }

    public static Registration createRegistration(Registration registration, KasnebCourse course) throws IOException, CustomHttpException, ParseException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        ObjectMapper mapper = new ObjectMapper();
        String jsonReq = mapper.writeValueAsString(registration);
        String jsonResponse = new RestUtil().doPost(Constants.CORE_BASE_URL + "api/registration/" + course.getId(), jsonReq);
        Registration created = gson.fromJson(jsonResponse, Registration.class);
        System.out.println(Constants.CORE_BASE_URL + "api/registration/" + course.getId());
        System.out.println(jsonReq);
        return created;
    }

    public static Receipt createReceipt(Receipt entity, KasnebCourse course) throws IOException, CustomHttpException, ParseException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        ObjectMapper mapper = new ObjectMapper();
        String jsonReq = mapper.writeValueAsString(entity);
        String jsonResponse = new RestUtil().doPost(Constants.CORE_BASE_URL + "api/receipt/" + course.getId(), jsonReq);
        Receipt created = gson.fromJson(jsonResponse, Receipt.class);
        System.out.println(Constants.CORE_BASE_URL + "api/receipt/" + course.getId());
        System.out.println(jsonReq);
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

    public static ExamEntry createExamEntry(ExamEntry examEntry, KasnebCourse course) throws JsonProcessingException, IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        ObjectMapper mapper = new ObjectMapper();
        String jsonReq = mapper.writeValueAsString(examEntry);
        String jsonResponse = new RestUtil().doPost(Constants.CORE_BASE_URL + "api/exam/" + course.getId(), jsonReq);
        ExamEntry created = gson.fromJson(jsonResponse, ExamEntry.class);
        System.out.println(Constants.CORE_BASE_URL + "api/exam/" + course.getId());
        System.out.println(jsonReq);
        return created;
    }

    public static void createExemption(List<CoreExemption> exemptions, KasnebCourse course) throws JsonProcessingException, IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        ObjectMapper mapper = new ObjectMapper();
        String jsonReq = mapper.writeValueAsString(exemptions);
        new RestUtil().doPost(Constants.CORE_BASE_URL + "api/exemption/" + course.getId(), jsonReq);
        System.out.println(jsonReq);
    }

    public static void createRenewal(List<Renewal> renewals, KasnebCourse course) throws JsonProcessingException, IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        ObjectMapper mapper = new ObjectMapper();
        String jsonReq = mapper.writeValueAsString(renewals);
        new RestUtil().doPost(Constants.CORE_BASE_URL + "api/renewal/" + course.getId(), jsonReq);
        System.out.println(jsonReq);
    }

    public static List<StudentCoursePaper> getElligiblePapers(StudentCourse studentCourse) throws IOException, CustomHttpException {
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
        String json = new RestUtil().doGet(Constants.CORE_BASE_URL + "api/studentcourse/" + studentCourse.getCourse().getId() + "?regNo=" + studentCourse.getRegistrationNumber());
        return gson.fromJson(json, new TypeToken<List<StudentCoursePaper>>() {
        }.getType());
    }

}
