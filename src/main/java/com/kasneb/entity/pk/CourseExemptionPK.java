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
    Integer qualificationId;
    @Column(name = "courseCode")
    Integer courseCode;
    @Column(name = "paperCode")
    String paperCode;

    public Integer getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(Integer qualificationId) {
        this.qualificationId = qualificationId;
    }

    public Integer getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(Integer courseCode) {
        this.courseCode = courseCode;
    }

    public String getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }
}
