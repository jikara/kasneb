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
public class TimetableDocument {

    private String registrationNumber;
    private String nameAndId;
    private String date;
    private String sitting;
    private String centreName;
    private String examinationDetails;
    private List<Paper> papers;

    public TimetableDocument() {
    }

    public TimetableDocument(String registrationNumber, String nameAndId, String date, String sitting, String centreName, String examinationDetails, List<Paper> papers) {
        this.registrationNumber = registrationNumber;
        this.nameAndId = nameAndId;
        this.date = date;
        this.sitting = sitting;
        this.centreName = centreName;
        this.examinationDetails = examinationDetails;
        this.papers = papers;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getNameAndId() {
        return nameAndId;
    }

    public void setNameAndId(String nameAndId) {
        this.nameAndId = nameAndId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSitting() {
        return sitting;
    }

    public void setSitting(String sitting) {
        this.sitting = sitting;
    }

    public String getCentreName() {
        return centreName;
    }

    public void setCentreName(String centreName) {
        this.centreName = centreName;
    }

    public String getExaminationDetails() {
        return examinationDetails;
    }

    public void setExaminationDetails(String examinationDetails) {
        this.examinationDetails = examinationDetails;
    }

    public List<Paper> getPapers() {
        return papers;
    }

    public void setPapers(List<Paper> papers) {
        this.papers = papers;
    }
}
