/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

import java.io.Serializable;

/**
 *
 * @author jikara
 */
public class StudentCoursePaper implements Serializable {

    private CpaRegistration registration;
    private CpaPaper paper;
    private Integer section;
    private Integer part;
    private String stream;

    public CpaRegistration getRegistration() {
        return registration;
    }

    public void setRegistration(CpaRegistration registration) {
        this.registration = registration;
    }

    public CpaPaper getPaper() {
        return paper;
    }

    public void setPaper(CpaPaper paper) {
        this.paper = paper;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public Integer getPart() {
        return part;
    }

    public void setPart(Integer part) {
        this.part = part;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }
}
