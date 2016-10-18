/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kasneb.entity.pk.StudentCourseExemptionPaperPK;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

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
    @Transient
    private Integer studentId;
    @ManyToOne
    @JoinColumn(name = "paperCode", referencedColumnName = "code", insertable = false, updatable = false)
    @JsonManagedReference
    private Paper paper;
    @ManyToOne(optional = false)
    @JoinColumns({
        @JoinColumn(name = "studentCourseId", referencedColumnName = "studentCourseId", insertable = false, updatable = false),
        @JoinColumn(name = "qualificationId", referencedColumnName = "qualificationId")
    })
    @JsonManagedReference
    private StudentCourseQualification qualification;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created", updatable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date created = new Date();
    @Column(name = "isVerified")
    private Boolean verified;
    @Column(name = "isPaid")
    private Boolean paid = false;
    @Basic(optional = false)
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private VerificationStatus status;
    @Column(name = "dateVerified")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateVerified;
    @ManyToOne
    @JoinColumn(name = "verifiedBy", referencedColumnName = "id")
    @JsonIgnore
    private User verifiedBy;
    @Column(name = "verifyRemarks")
    private String verifyRemarks;
    @ManyToOne
    @JoinColumn(name = "invoiceId", referencedColumnName = "id")
    private ExemptionInvoice exemptionInvoice;
    @OneToOne(mappedBy = "studentCourseExemptionPaper")
    private ExemptionInvoiceDetail exemptionInvoiceDetail;
    @Column(name = "datePaid")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date datePaid;
    @Transient
    private String exemptionType;

    public StudentCourseExemptionPaper() {
    }

    public StudentCourseExemptionPaper(StudentCourseExemptionPaperPK studentCourseQualificationExemptionPK) {
        this.studentCourseQualificationExemptionPK = studentCourseQualificationExemptionPK;
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

    public String getExemptionType() {
        if (getQualification() != null) {
            exemptionType = getQualification().getType();
        }
        return exemptionType;
    }

    public void setExemptionType(String exemptionType) {
        this.exemptionType = exemptionType;
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

    public Date getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(Date datePaid) {
        this.datePaid = datePaid;
    }

    public User getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(User verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public String getVerifyRemarks() {
        return verifyRemarks;
    }

    public void setVerifyRemarks(String verifyRemarks) {
        this.verifyRemarks = verifyRemarks;
    }

    public Integer getStudentId() {
        if (getStudentCourse() != null) {
            studentId = getStudentCourse().getStudent().getId();
        }
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        created = new Date();
        this.created = created;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public ExemptionInvoice getExemptionInvoice() {
        return exemptionInvoice;
    }

    public void setExemptionInvoice(ExemptionInvoice exemptionInvoice) {
        this.exemptionInvoice = exemptionInvoice;
    }

}
