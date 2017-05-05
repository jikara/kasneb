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
public class Exemption {

    private ExemptionPK pk;
    private Integer regNo;
    private Integer section;
    private String paperCode;
    private String reference;
    private String date;

    public ExemptionPK getPk() {
        return pk;
    }

    public void setPk(ExemptionPK pk) {
        this.pk = pk;
    }

    public Integer getRegNo() {
        return regNo;
    }

    public void setRegNo(Integer regNo) {
        this.regNo = regNo;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public String getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
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

    public class ExemptionPK {

        private Integer regNo;
        private Integer section;
        private String paperCode;

        public Integer getRegNo() {
            return regNo;
        }

        public void setRegNo(Integer regNo) {
            this.regNo = regNo;
        }

        public Integer getSection() {
            return section;
        }

        public void setSection(Integer section) {
            this.section = section;
        }

        public String getPaperCode() {
            return paperCode;
        }

        public void setPaperCode(String paperCode) {
            this.paperCode = paperCode;
        }
    }
}
