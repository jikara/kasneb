/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity.pk;

import java.io.Serializable;
import java.util.Objects;
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
    @Column(name = "paperCode", insertable = false, updatable = false)
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.paperCode);
        hash = 29 * hash + Objects.hashCode(this.studentCourseSittingId);
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
        final StudentCourseSittingPaperPK other = (StudentCourseSittingPaperPK) obj;
        if (!Objects.equals(this.paperCode, other.paperCode)) {
            return false;
        }
        return Objects.equals(this.studentCourseSittingId, other.studentCourseSittingId);
    }
}
