/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity.pk;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author jikara
 */
@Embeddable
public class StudentCourseExemptionPaperPK implements Serializable {

    @Column(name = "studentCourseId")
    private Integer studentCourseId;
    @Column(name = "paperCode")
    private String paperCode;

    public StudentCourseExemptionPaperPK() {
    }

    public StudentCourseExemptionPaperPK(Integer studentCourseId, String paperCode) {
        this.studentCourseId = studentCourseId;
        this.paperCode = paperCode;
    }

    public Integer getStudentCourseId() {
        return studentCourseId;
    }

    public void setStudentCourseId(Integer studentCourseId) {
        this.studentCourseId = studentCourseId;
    }

    public String getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.studentCourseId);
        hash = 41 * hash + Objects.hashCode(this.paperCode);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StudentCourseExemptionPaperPK other = (StudentCourseExemptionPaperPK) obj;
        if (!Objects.equals(this.paperCode, other.paperCode)) {
            return false;
        }
        if (!Objects.equals(this.studentCourseId, other.studentCourseId)) {
            return false;
        }
        return true;
    }

}
