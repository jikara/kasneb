/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

/**
 *
 * @author jikara
 */
public class ExamBooking {

    private Integer id;
    private Integer regNo;
    private String examCode;
    private String examDescription;
    private Integer examYear;
    private Integer examSitting;
    private Receipt receipt;
    private Centre centre;
    private String centreName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRegNo() {
        return regNo;
    }

    public void setRegNo(Integer regNo) {
        this.regNo = regNo;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    public String getExamDescription() {
        return examDescription;
    }

    public void setExamDescription(String examDescription) {
        this.examDescription = examDescription;
    }

    public Integer getExamYear() {
        return examYear;
    }

    public void setExamYear(Integer examYear) {
        this.examYear = examYear;
    }

    public Integer getExamSitting() {
        return examSitting;
    }

    public void setExamSitting(Integer examSitting) {
        this.examSitting = examSitting;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public Centre getCentre() {
        return centre;
    }

    public void setCentre(Centre centre) {
        this.centre = centre;
    }

    public String getCentreName() {
        return centreName;
    }

    public void setCentreName(String centreName) {
        this.centreName = centreName;
    }
}
