/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "exemption")
public class Exemption implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "reference")
    private String reference;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Nairobi")
    @Column(name = "created", updatable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date created = new Date();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Nairobi")
    @Column(name = "dateVerified")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateVerified;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "studentCourseId", referencedColumnName = "id", nullable = false)
    private StudentCourse studentCourse;
    @Transient
    private Student student;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "exemptionQualification",
            joinColumns = {
                @JoinColumn(name = "exemptionId", referencedColumnName = "id")},
            inverseJoinColumns
            = @JoinColumn(name = "qualificationId", referencedColumnName = "id"))

    private List<Course> qualifications;
    @Column(name = "courseName")
    private String courseName;
    @Basic(optional = false)
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExemptionType type;
    @ManyToOne
    @JoinColumn(name = "institutionId", referencedColumnName = "id")
    private Institution institution;
    @Column(name = "institutionName")
    private String institutionName;

    @OneToMany(mappedBy = "exemption", cascade = CascadeType.ALL)
    private List<ExemptionDocument> documents;

    @OneToMany(mappedBy = "exemption", cascade = CascadeType.ALL)
    private List<ExemptionPaper> papers;
    @Transient
    private List<ExemptionPaper> pendingPapers;
    @ManyToOne
    @JoinColumn(name = "invoiceId", referencedColumnName = "id")
    private Invoice invoice;
    @Basic(optional = false)
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    ExemptionStatus status = ExemptionStatus.PENDING;
    @Column(name = "verified")
    private Boolean verified = false;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User verifiedBy;
    @Transient
    private String fullRegNo;
    @Transient
    private Integer studentCourseId;

    public Exemption() {
    }

    public Exemption(StudentCourse studentCourse, List<ExemptionPaper> papers) {
        this.studentCourse = studentCourse;
        this.papers = papers;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public StudentCourse getStudentCourse() {
        if (studentCourse != null) {
            this.fullRegNo = studentCourse.getFullRegistrationNumber();
        }
        return studentCourse;
    }

    public void setStudentCourse(StudentCourse studentCourse) {
        this.studentCourse = studentCourse;
    }

    public List<Course> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<Course> qualifications) {
        this.qualifications = qualifications;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public ExemptionType getType() {
        return type;
    }

    public void setType(ExemptionType type) {
        this.type = type;
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
        this.institutionName = institutionName;
    }

    public List<ExemptionDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<ExemptionDocument> documents) {
        for (ExemptionDocument document : documents) {
            document.setExemption(this);
        }
        this.documents = documents;
    }

    public List<ExemptionPaper> getPendingPapers() {
        pendingPapers = new ArrayList<>();
        if (getPapers() != null) {
            for (ExemptionPaper exemptionPaper : getPapers()) {
                if (exemptionPaper.getVerificationStatus() != null && exemptionPaper.getVerificationStatus().equals(VerificationStatus.PENDING)) {
                    pendingPapers.add(exemptionPaper);
                }
            }
        }
        return pendingPapers;
    }

    public List<ExemptionPaper> getPapers() {
        return papers;
    }

    public void setPapers(List<ExemptionPaper> papers) {
        for (ExemptionPaper paper : papers) {
            paper.setExemption(this);
        }
        this.papers = papers;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public ExemptionStatus getStatus() {
        return status;
    }

    public void setStatus(ExemptionStatus status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getDateVerified() {
        return dateVerified;
    }

    public void setDateVerified(Date dateVerified) {
        this.dateVerified = dateVerified;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public User getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(User verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public Student getStudent() {
        if (getStudentCourse() != null) {
            student = getStudentCourse().getStudentObj();
        }
        return student;
    }

    public String getFullRegNo() {
        return fullRegNo;
    }

    public Integer getStudentCourseId() {
        if (getStudentCourse() != null) {
            studentCourseId = getStudentCourse().getId();
        }
        return studentCourseId;
    }

}
