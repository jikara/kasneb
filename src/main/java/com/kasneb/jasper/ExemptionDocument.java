/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.jasper;

import java.util.List;

/**
 *
 * @author jikara
 */
public class ExemptionDocument {

    private String reference;
    private String date;
    private String studentAndReg;
    private String studentName;
    private String regNo;
    private String address;
    private String city;
    private String country;
    private String issuedBy;
    private List<Exemption> exemptions;
    private String letterTitle;

    public ExemptionDocument() {
    }

    public ExemptionDocument(String reference, String date, String regNo, String studentName, String address, String city, String country, String issuedBy, List<Exemption> exemptions) {
        this.reference = reference;
        this.date = date;
        this.regNo = regNo;
        this.studentName = studentName;
        this.address = address;
        this.city = city;
        this.country = country;
        this.issuedBy = issuedBy;
        this.exemptions = exemptions;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStudentAndReg() {
        studentAndReg = getStudentName() + " - " + getRegNo();
        return studentAndReg;
    }

    public void setStudentAndReg(String studentAndReg) {
        this.studentAndReg = studentAndReg;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public List<Exemption> getExemptions() {
        return exemptions;
    }

    public void setExemptions(List<Exemption> exemptions) {
        this.exemptions = exemptions;
    }

    public String getLetterTitle() {
        letterTitle = "EXEMPTION IN THE CPA EXAMINATION - " + getRegNo();
        return letterTitle;
    }

    public void setLetterTitle(String letterTitle) {
        this.letterTitle = letterTitle;
    }
}
