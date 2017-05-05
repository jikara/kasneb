/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity.pk;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author jikara
 */
@Embeddable
public class CourseExemptionPK implements Serializable {

    @Column(name = "qualificationId")
    String qualificationId;
    @Column(name = "courseId")
    String courseId;
    @Column(name = "paperCode")
    String paperCode;

    public CourseExemptionPK() {
    }

    public CourseExemptionPK(String qualificationId, String courseId, String paperCode) {
        this.qualificationId = qualificationId;
        this.courseId = courseId;
        this.paperCode = paperCode;
    }

    public String getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(String qualificationId) {
        this.qualificationId = qualificationId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }
}
