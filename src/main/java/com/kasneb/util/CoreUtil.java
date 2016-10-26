/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasneb.client.Course;
import com.kasneb.client.CpaRegistration;
import com.kasneb.client.CsQualification;
import com.kasneb.client.CsSex;
import com.kasneb.client.LearnAbout;
import com.kasneb.client.Nation;
import com.kasneb.client.Stream;
import com.kasneb.entity.Student;
import com.kasneb.entity.StudentCourse;
import java.io.IOException;
import java.util.Date;

/**
 *
 * @author jikara
 */
public class CoreUtil {

    public static String generateRegistrationNumber(StudentCourse studentCourse) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Student student = studentCourse.getStudent();
        //Create core object
        CpaRegistration r = new CpaRegistration(null, Stream.AC, studentCourse.getCreated(), student.getLastName(), studentCourse.getStudent().getFirstName(), student.getMiddleName(), "", new CsSex("M"), student.getDob(), new Nation(student.getCountryId().getCode()), student.getDocumentNo(), new CsQualification(1), null, "C1135308", "", "", student.getContact().getPostalAddress(), "", student.getContact().getPostalAddress(), student.getContact().getTown(), student.getContact().getCountryId().getName(), student.getEmail(), student.getPhoneNumber(), "", new Course("00"), "Media", new LearnAbout(4), new Nation("1"), new CsQualification(5));
        RestUtil.doPost("http://localhost:29097/core/api/cpa", mapper.writeValueAsString(r));
        return "NAC/1121322";
    }
}
