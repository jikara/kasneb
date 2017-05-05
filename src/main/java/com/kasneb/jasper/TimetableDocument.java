/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.jasper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private String fullName;
    private String address;
    private String town;
    private String country;

    public TimetableDocument() {
    }

    public TimetableDocument(String registrationNumber, String nameAndId, String date, String sitting, String centreName, String fullName, String address, String town, String country) {
        this.registrationNumber = registrationNumber;
        this.nameAndId = nameAndId;
        this.date = date;
        this.sitting = sitting;
        this.centreName = centreName;
        this.fullName = fullName;
        this.address = address;
        this.town = town;
        this.country = country;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Paper> getPapers() {
        Collections.sort(papers, new Comparator<Paper>() {
            @Override
            public int compare(Paper p1, Paper p2) {
                return p1.getCode().compareTo(p2.getCode());
            }
        });
        return papers;
    }

    public void setPapers(List<Paper> papers) {
        this.papers = papers;
    }

    public void addPapers(Paper paper) {
        List<Paper> myPapers = new ArrayList<>();
        myPapers.add(paper);
        this.papers = myPapers;
    }

    public class Paper {

        private String code;
        private String name;
        private String date;
        private String time;

        public Paper() {
        }

        public Paper(String code, String name, String date, String time) {
            this.code = code;
            this.name = name;
            this.date = date;
            this.time = time;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
