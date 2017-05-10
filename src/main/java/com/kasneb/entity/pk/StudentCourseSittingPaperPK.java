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
    @Column(name = "paperCode")
    protected String paperCode;
    @Basic(optional = false)
    @Column(name = "studentCourseSittingId")
    protected Integer studentCourseSittingId;

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
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.paperCode);
        hash = 83 * hash + Objects.hashCode(this.studentCourseSittingId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StudentCourseSittingPaperPK)) {
            return false;
        }
        StudentCourseSittingPaperPK pk = (StudentCourseSittingPaperPK) obj;
        return Objects.equals(this.studentCourseSittingId, pk.studentCourseSittingId) && pk.paperCode.equals(this.paperCode);
    }
}
