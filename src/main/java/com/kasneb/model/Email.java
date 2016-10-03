/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.model;

import java.util.Map;

/**
 *
 * @author jikara
 */
public class Email {

    private String address;
    private String subject;
    private Map emailBody;

    public Email() {
    }

    public Email(String address, String subject, Map emailBody) {
        this.address = address;
        this.subject = subject;
        this.emailBody = emailBody;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Map getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(Map emailBody) {
        this.emailBody = emailBody;
    }
}
