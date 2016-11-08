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
public class ReceiptDocument {

    private String referenceNumber;
    private String fullName;
    private String address;
    private String town;
    private String country;
    private String receiptNumber;
    private String date;
    private String registrationNumber;
    private String courseName;
    private List<ReceiptItem> items;

    public ReceiptDocument() {
    }

    public ReceiptDocument(String referenceNumber, String fullName, String address, String town, String country, String receiptNumber, String date, String registrationNumber, String courseName, List<ReceiptItem> items) {
        this.referenceNumber = referenceNumber;
        this.fullName = fullName;
        this.address = address;
        this.town = town;
        this.country = country;
        this.receiptNumber = receiptNumber;
        this.date = date;
        this.registrationNumber = registrationNumber;
        this.courseName = courseName;
        this.items = items;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
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

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<ReceiptItem> getItems() {
        return items;
    }

    public void setItems(List<ReceiptItem> items) {
        this.items = items;
    }

}
