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
public class StudentCourseSubscriptionPK implements Serializable {

    @Basic
    @Column(name = "studentCourseId")
    private Integer studentCourseId;
    @Basic
    @Column(name = "rYear")
    private Integer year;

    public StudentCourseSubscriptionPK() {
    }

    public StudentCourseSubscriptionPK(Integer studentCourseId, Integer year) {
        this.studentCourseId = studentCourseId;
        this.year = year;
    }

    public Integer getStudentCourseId() {
        return studentCourseId;
    }

    public void setStudentCourseId(Integer studentCourseId) {
        this.studentCourseId = studentCourseId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.studentCourseId);
        hash = 79 * hash + Objects.hashCode(this.year);
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
        final StudentCourseSubscriptionPK other = (StudentCourseSubscriptionPK) obj;
        return Objects.equals(this.studentCourseId, other.studentCourseId) && Objects.equals(this.studentCourseId, other.studentCourseId);
    }
}
