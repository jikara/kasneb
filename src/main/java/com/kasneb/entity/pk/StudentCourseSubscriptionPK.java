/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity.pk;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;

/**
 *
 * @author jikara
 */

public class StudentCourseSubscriptionPK implements Serializable {

    @Column(name = "studentCourseId", insertable = false, updatable = false)
    private Integer studentCourseId;
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
        if (!Objects.equals(this.studentCourseId, other.studentCourseId)) {
            return false;
        }
        return Objects.equals(this.year, other.year);
    }
}
