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

    private Integer id;
    private Registration egistration;
    private Integer section;
    private String stream;
    private String paperCode;
    private String flaq;
    private String altered;
    private String reference;
    private String date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Registration getEgistration() {
        return egistration;
    }

    public void setEgistration(Registration egistration) {
        this.egistration = egistration;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }

    public String getFlaq() {
        return flaq;
    }

    public void setFlaq(String flaq) {
        this.flaq = flaq;
    }

    public String getAltered() {
        return altered;
    }

    public void setAltered(String altered) {
        this.altered = altered;
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
}
