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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "studentCourseQualification")
public class StudentCourseQualification implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private StudentCourseQualificationPK studentCourseQualificationPK;
    @ManyToOne
    @JoinColumn(name = "studentCourseId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonManagedReference
    @JsonIgnore
    private StudentCourse studentCourse;
    @ManyToOne
    @JoinColumn(name = "qualificationId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonManagedReference
    private Qualification qualification;
    @Basic(optional = false)
    @Column(name = "document", nullable = false)
    private String document;
    @ManyToOne
    @JoinColumn(name = "institutionId", referencedColumnName = "id")
    @JsonManagedReference
    private Institution institution;
    @Column(name = "institutionName")
    private String institutionName;  //specified

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

    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        if (this.institution != null) {
            this.institutionName = null;
        } else {
            this.institutionName = institutionName;
        }
    }

}
