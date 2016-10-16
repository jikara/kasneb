/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kasneb.entity.pk.StudentCourseExemptionPaperPK;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "studentCourseExemptionPaper")
public class StudentCourseExemptionPaper implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    @JsonIgnore
    private StudentCourseExemptionPaperPK studentCourseQualificationExemptionPK;
    @ManyToOne
    @JoinColumn(name = "studentCourseId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonBackReference
    private StudentCourse studentCourse;
    @ManyToOne
    @JoinColumn(name = "paperCode", referencedColumnName = "code", insertable = false, updatable = false)
    private Paper paper;
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "studentCourseId", referencedColumnName = "studentCourseId", insertable = false, updatable = false),
        @JoinColumn(name = "qualificationId", referencedColumnName = "qualificationId")
    })
    @JsonIgnore
    private StudentCourseQualification qualification;
    @Column(name = "isVerified")
    private Boolean verified;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private VerificationStatus status;
    @Column(name = "dateVerified")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateVerified;
    @ManyToOne
    @JoinColumn(name = "verifiedBy", referencedColumnName = "id")
    @JsonIgnore
    private User verifiedBy;

    public StudentCourseExemptionPaper() {
    }

    public StudentCourseExemptionPaper(StudentCourseExemptionPaperPK studentCourseQualificationExemptionPK, StudentCourse studentCourse, Paper paper, StudentCourseQualification qualification, Boolean verified, VerificationStatus status) {
        this.studentCourseQualificationExemptionPK = studentCourseQualificationExemptionPK;
        this.studentCourse = studentCourse;
        this.paper = paper;
        this.qualification = qualification;
        this.verified = verified;
        this.status = status;
    }

    public StudentCourseExemptionPaperPK getStudentCourseQualificationExemptionPK() {
        return studentCourseQualificationExemptionPK;
    }

    public void setStudentCourseQualificationExemptionPK(StudentCourseExemptionPaperPK studentCourseQualificationExemptionPK) {
        this.studentCourseQualificationExemptionPK = studentCourseQualificationExemptionPK;
    }

    public StudentCourse getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(StudentCourse studentCourse) {
        this.studentCourse = studentCourse;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public StudentCourseQualification getQualification() {
        return qualification;
    }

    public void setQualification(StudentCourseQualification qualification) {
        this.qualification = qualification;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public VerificationStatus getStatus() {
        return status;
    }

    public void setStatus(VerificationStatus status) {
        this.status = status;
    }

    public Date getDateVerified() {
        return dateVerified;
    }

    public void setDateVerified(Date dateVerified) {
        this.dateVerified = dateVerified;
    }

    public User getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(User verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

}
