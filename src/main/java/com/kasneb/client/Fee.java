/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author jikara
 */
public class Fee implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String courseCode;
    private String currencyCode;
    private BigDecimal registrationFee;
    private BigDecimal lateRegistrationFee;
    private BigDecimal renewalFee;
    private BigDecimal reinstatementFee;
    private BigDecimal idCardFee;
    private Date lastEdited;
    private Course course;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(BigDecimal registrationFee) {
        this.registrationFee = registrationFee;
    }

    public BigDecimal getLateRegistrationFee() {
        return lateRegistrationFee;
    }

    public void setLateRegistrationFee(BigDecimal lateRegistrationFee) {
        this.lateRegistrationFee = lateRegistrationFee;
    }

    public BigDecimal getRenewalFee() {
        return renewalFee;
    }

    public void setRenewalFee(BigDecimal renewalFee) {
        this.renewalFee = renewalFee;
    }

    public BigDecimal getReinstatementFee() {
        return reinstatementFee;
    }

    public void setReinstatementFee(BigDecimal reinstatementFee) {
        this.reinstatementFee = reinstatementFee;
    }

    public BigDecimal getIdCardFee() {
        return idCardFee;
    }

    public void setIdCardFee(BigDecimal idCardFee) {
        this.idCardFee = idCardFee;
    }

    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Fee)) {
            return false;
        }
        Fee other = (Fee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kasneb.entity.Fee[ id=" + id + " ]";
    }

}
