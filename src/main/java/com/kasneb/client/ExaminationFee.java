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
    private Integer levelId;
    private BigDecimal sectionExamFee;
    private BigDecimal paperExamFee;
    private BigDecimal sectionLateExamFee;
    private BigDecimal paperLateExamFee;
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

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
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

    public BigDecimal getSectionLateExamFee() {
        return sectionLateExamFee;
    }

    public void setSectionLateExamFee(BigDecimal sectionLateExamFee) {
        this.sectionLateExamFee = sectionLateExamFee;
    }

    public BigDecimal getPaperLateExamFee() {
        return paperLateExamFee;
    }

    public void setPaperLateExamFee(BigDecimal paperLateExamFee) {
        this.paperLateExamFee = paperLateExamFee;
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
