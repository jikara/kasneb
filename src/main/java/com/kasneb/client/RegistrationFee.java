/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author jikara
 */
public class RegistrationFee {

    private Integer id;
    private Course course;
    private Currency currency;
    private BigDecimal registrationFee;
    private BigDecimal lateRegistrationFee;
    private BigDecimal registrationRenewalFee;
    private BigDecimal registrationReinstatementFee;
    private BigDecimal studentIdFee;
    private Date lastEdited;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
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

    public BigDecimal getRegistrationRenewalFee() {
        return registrationRenewalFee;
    }

    public void setRegistrationRenewalFee(BigDecimal registrationRenewalFee) {
        this.registrationRenewalFee = registrationRenewalFee;
    }

    public BigDecimal getRegistrationReinstatementFee() {
        return registrationReinstatementFee;
    }

    public void setRegistrationReinstatementFee(BigDecimal registrationReinstatementFee) {
        this.registrationReinstatementFee = registrationReinstatementFee;
    }

    public BigDecimal getStudentIdFee() {
        return studentIdFee;
    }

    public void setStudentIdFee(BigDecimal studentIdFee) {
        this.studentIdFee = studentIdFee;
    }

    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }
}
