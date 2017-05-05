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
public class CoreExemption {

    public ExemptionPK pk;
    public String stream;
    public String flaq;
    public String reference;

    public CoreExemption(String stream, String flaq, String reference) {
        this.stream = stream;
        this.flaq = flaq;
        this.reference = reference;
    }

    public ExemptionPK getPk() {
        return pk;
    }

    public void setPk(ExemptionPK pk) {
        this.pk = pk;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getFlaq() {
        return flaq;
    }

    public void setFlaq(String flaq) {
        this.flaq = flaq;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public class ExemptionPK {

        public Integer regNo;
        public Integer section;
        public Integer level;
        public String paperCode;

        public ExemptionPK(Integer regNo, Integer section, Integer level, String paperCode) {
            this.regNo = regNo;
            this.section = section;
            this.level = level;
            this.paperCode = paperCode;
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

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public void setPaperCode(String paperCode) {
            this.paperCode = paperCode;
        }
    }
}
