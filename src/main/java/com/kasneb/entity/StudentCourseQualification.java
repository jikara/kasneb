/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.kasneb.entity.pk.StudentCourseQualificationPK;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "studentCourseQualification")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public class StudentCourseQualification implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private StudentCourseQualificationPK studentCourseQualificationPK;
    @ManyToOne(optional = false)
    @JoinColumn(name = "studentCourseId", referencedColumnName = "id", insertable = false, updatable = false)
    private StudentCourse studentCourse;
    @ManyToOne(optional = false)
    @JoinColumn(name = "qualificationId", referencedColumnName = "id", insertable = false, updatable = false)
    private Course qualification;

    public StudentCourseQualification() {
    }

    public StudentCourseQualificationPK getStudentCourseQualificationPK() {
        return studentCourseQualificationPK;
    }

    public void setStudentCourseQualificationPK(StudentCourseQualificationPK studentCourseQualificationPK) {
        this.studentCourseQualificationPK = studentCourseQualificationPK;
    }

    public StudentCourse getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(StudentCourse studentCourse) {
        this.studentCourse = studentCourse;
    }

    public Course getQualification() {
        return qualification;
    }

    public void setQualification(Course qualification) {
        this.qualification = qualification;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.studentCourseQualificationPK);
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
        final StudentCourseQualification other = (StudentCourseQualification) obj;
        if (!Objects.equals(this.studentCourseQualificationPK, other.studentCourseQualificationPK)) {
            return false;
        }
        return true;
    }

}
