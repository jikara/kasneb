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
public class StudentQualificationPK implements Serializable {

    @Column(name = "studentId")
    private Integer studentId;
    @Column(name = "qualificationId")
    private String qualificationId;

    public StudentQualificationPK() {
    }

    public StudentQualificationPK(String qualificationId) {
        this.qualificationId = qualificationId;
    }

    public StudentQualificationPK(Integer studentId, String qualificationId) {
        this.studentId = studentId;
        this.qualificationId = qualificationId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(String qualificationId) {
        this.qualificationId = qualificationId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.studentId);
        hash = 37 * hash + Objects.hashCode(this.qualificationId);
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
        final StudentQualificationPK other = (StudentQualificationPK) obj;
        if (!Objects.equals(this.qualificationId, other.qualificationId)) {
            return false;
        }
        if (!Objects.equals(this.studentId, other.studentId)) {
            return false;
        }
        return true;
    }

}
