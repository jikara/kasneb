/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

import java.math.BigDecimal;

/**
 *
 * @author jikara
 */
public class ExaminationFee {

    private Integer id;
    private Integer sectionId;
    private BigDecimal sectionExamFee;
    private BigDecimal paperExamFee;
    private BigDecimal lateSectionExamFee;
    private BigDecimal latePaperExamFee;
    private BigDecimal exemptionFee;
    private BigDecimal remarkingFee;
    private Boolean current;
    private Currency currency;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public BigDecimal getSectionExamFee() {
        return sectionExamFee;
    }

    public void setSectionExamFee(BigDecimal sectionExamFee) {
        this.sectionExamFee = sectionExamFee;
    }

    public BigDecimal getPaperExamFee() {
        return paperExamFee;
    }

    public void setPaperExamFee(BigDecimal paperExamFee) {
        this.paperExamFee = paperExamFee;
    }

    public BigDecimal getLateSectionExamFee() {
        return lateSectionExamFee;
    }

    public void setLateSectionExamFee(BigDecimal lateSectionExamFee) {
        this.lateSectionExamFee = lateSectionExamFee;
    }

    public BigDecimal getLatePaperExamFee() {
        return latePaperExamFee;
    }

    public void setLatePaperExamFee(BigDecimal latePaperExamFee) {
        this.latePaperExamFee = latePaperExamFee;
    }

    public BigDecimal getExemptionFee() {
        return exemptionFee;
    }

    public void setExemptionFee(BigDecimal exemptionFee) {
        this.exemptionFee = exemptionFee;
    }

    public BigDecimal getRemarkingFee() {
        return remarkingFee;
    }

    public void setRemarkingFee(BigDecimal remarkingFee) {
        this.remarkingFee = remarkingFee;
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
