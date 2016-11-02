/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

import java.io.Serializable;
import java.math.BigDecimal;

public class CpaExaminationFee implements Serializable {

    private Integer id;
    private Integer section;
    private BigDecimal sectionFee;
    private BigDecimal paperFee;
    private BigDecimal lateSectionFee;
    private BigDecimal latePaperFee;
    private Boolean current;
    private Currency currency;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public BigDecimal getSectionFee() {
        return sectionFee;
    }

    public void setSectionFee(BigDecimal sectionFee) {
        this.sectionFee = sectionFee;
    }

    public BigDecimal getPaperFee() {
        return paperFee;
    }

    public void setPaperFee(BigDecimal paperFee) {
        this.paperFee = paperFee;
    }

    public BigDecimal getLateSectionFee() {
        return lateSectionFee;
    }

    public void setLateSectionFee(BigDecimal lateSectionFee) {
        this.lateSectionFee = lateSectionFee;
    }

    public BigDecimal getLatePaperFee() {
        return latePaperFee;
    }

    public void setLatePaperFee(BigDecimal latePaperFee) {
        this.latePaperFee = latePaperFee;
    }

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
