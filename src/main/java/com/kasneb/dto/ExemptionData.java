/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.dto;

import com.kasneb.entity.KasnebCourse;
import com.kasneb.entity.OtherCourse;
import com.kasneb.entity.Paper;

/**
 *
 * @author jikara
 */
public class ExemptionData {

    public Paper paper;
    public OtherCourse qualification;
    public KasnebCourse course;

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public OtherCourse getQualification() {
        return qualification;
    }

    public void setQualification(OtherCourse qualification) {
        this.qualification = qualification;
    }

    public KasnebCourse getCourse() {
        return course;
    }

    public void setCourse(KasnebCourse course) {
        this.course = course;
    }
}
