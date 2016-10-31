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
public class StudentCourseQualificationPK implements Serializable {

    @Column(name = "studentCourseId")
    private Integer studentCourseId;
    @Column(name = "qualificationId")
    private String qualificationId;

    public Integer getStudentCourseId() {
        return studentCourseId;
    }

    public void setStudentCourseId(Integer studentCourseId) {
        this.studentCourseId = studentCourseId;
    }

    public String getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(String qualificationId) {
        this.qualificationId = qualificationId;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.studentCourseId);
        hash = 83 * hash + Objects.hashCode(this.qualificationId);
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
        final StudentCourseQualificationPK other = (StudentCourseQualificationPK) obj;
        if (!Objects.equals(this.studentCourseId, other.studentCourseId)) {
            return false;
        }
        return Objects.equals(this.qualificationId, other.qualificationId);
    }

}
