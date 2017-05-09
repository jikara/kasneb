/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.joda.time.DateTime;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "studentCourseSubscription")
@IdClass(com.kasneb.entity.pk.StudentCourseSubscriptionPK.class)
public class StudentCourseSubscription implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "studentCourseId", insertable = false, updatable = false)
    private Integer studentCourseId;
    @Id
    @Column(name = "rYear")
    private Integer year;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "studentCourseId", referencedColumnName = "id")
    private StudentCourse studentCourse;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Nairobi")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false, updatable = false)
    private Date created = new Date();
    @Basic(optional = false)
    @Column(name = "expiry", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Nairobi")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date expiry;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "invoiceId", referencedColumnName = "id")
    private Invoice invoice;
    @Transient
    private Boolean current;
    @Transient
    private Integer endYear;
    @Transient
    private Date renewalDate;

    public StudentCourseSubscription() {
    }

    public StudentCourseSubscription(Integer year, StudentCourse studentCourse) {
        this.year = year;
        this.studentCourse = studentCourse;
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

    public StudentCourse getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(StudentCourse studentCourse) {
        this.studentCourse = studentCourse;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Boolean isCurrent() {
        if (new Date().before(getExpiry())) {
            current = true;
        }
        return current;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public Date getRenewalDate() {
        if (getExpiry() != null) {
            DateTime nextRenewalDate = new DateTime(getExpiry());
            nextRenewalDate = nextRenewalDate.plusDays(1);
            return nextRenewalDate.toDate();
        }
        return null;
    }

    public Integer getEndYear() {
        if (getYear() != null) {
            endYear = getYear() + 1;
        }
        return endYear;
    }

}
