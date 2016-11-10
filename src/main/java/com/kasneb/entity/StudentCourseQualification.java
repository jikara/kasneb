/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kasneb.entity.pk.StudentCourseQualificationPK;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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
    @JoinColumn(name = "studentCourseId", referencedColumnName = "id")
    @JsonIgnore
    private StudentCourse studentCourse;
    @ManyToOne(optional = false)
    @JoinColumn(name = "qualificationId", referencedColumnName = "id")
    @JsonManagedReference
    private Course qualification;
    @Transient
    protected String type;

    public StudentCourseQualification() {
    }

    public StudentCourseQualification(StudentCourseQualificationPK studentCourseQualificationPK) {
        this.studentCourseQualificationPK = studentCourseQualificationPK;
    }

    public StudentCourseQualificationPK getStudentCourseQualificationPK() {
        return studentCourseQualificationPK;
    }

    public StudentCourseQualification(Course qualification) {
        this.qualification = qualification;
    }

    public void setStudentCourseQualificationPK(StudentCourseQualificationPK studentCourseQualificationPK) {
        this.studentCourseQualificationPK = studentCourseQualificationPK;
    }

    public StudentCourseQualification(StudentCourse studentCourse, Course qualification) {
        this.studentCourse = studentCourse;
        this.qualification = qualification;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
