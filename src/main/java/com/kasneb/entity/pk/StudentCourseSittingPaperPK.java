/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity.pk;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author jikara
 */
@Embeddable
public class StudentCourseSittingPaperPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "paperCode")
    private String paperCode;
    @Basic(optional = false)
    @Column(name = "studentCourseSittingId", insertable = false, updatable = false)
    private Integer studentCourseSittingId;

    public StudentCourseSittingPaperPK() {
    }

    public StudentCourseSittingPaperPK(String paperCode, Integer studentCourseSittingId) {
        this.paperCode = paperCode;
        this.studentCourseSittingId = studentCourseSittingId;
    }

    public String getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }

    public Integer getStudentCourseSittingId() {
        return studentCourseSittingId;
    }

    public void setStudentCourseSittingId(Integer studentCourseSittingId) {
        this.studentCourseSittingId = studentCourseSittingId;
    }
}
