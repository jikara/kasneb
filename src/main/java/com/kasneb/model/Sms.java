/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.model;

/**
 *
 * @author jikara
 */
public class Sms {

    private String phoneNumber;
    private String message;

    public Sms() {
    }

    public Sms(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    public String getPhoneNumber() {
        phoneNumber = "254" + phoneNumber.substring(Math.max(phoneNumber.length() - 9, 0));
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
