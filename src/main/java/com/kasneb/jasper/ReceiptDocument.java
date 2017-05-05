/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.jasper;

import com.kasneb.entity.ExamInvoiceDetail;
import com.kasneb.entity.ExemptionInvoiceDetail;
import com.kasneb.entity.InvoiceDetail;
import com.kasneb.entity.RenewalInvoiceDetail;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author jikara
 */
public class ReceiptDocument {

    private BigDecimal total;
    private String totalString;
    private String referenceNumber;
    private String fullName;
    private String address;
    private String town;
    private String country;
    private String receiptNumber;
    private String date;
    private String registrationNumber;
    private String courseName;
    private List<InvoiceDetail> items;
    private String type;
    private String centre;
    private String firstExamDate;
    private String firstRenewalDate;

    public ReceiptDocument() {
    }

    public ReceiptDocument(BigDecimal total, String referenceNumber, String fullName, String address, String town, String country, String receiptNumber, String date, String registrationNumber, String courseName, List<InvoiceDetail> items) {
        this.total = total;
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

    public BigDecimal getTotal() {
        return new BigDecimal(total.toBigInteger().toString());
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCentre() {
        return centre;
    }

    public void setCentre(String centre) {
        this.centre = centre;
    }

    public String getFirstExamDate() {
        return firstExamDate;
    }

    public void setFirstExamDate(String firstExamDate) {
        this.firstExamDate = firstExamDate;
    }

    public String getFirstRenewalDate() {
        return firstRenewalDate;
    }

    public void setFirstRenewalDate(String firstRenewalDate) {
        this.firstRenewalDate = firstRenewalDate;
    }

    public String getTotalString() {
        if (total != null) {
            DecimalFormat df = new DecimalFormat("#,###");
            totalString = df.format(total);
        }
        return totalString;
    }

    public List<InvoiceDetail> getItems() {
        Collections.sort(items, new Comparator<InvoiceDetail>() {
            @Override
            public int compare(InvoiceDetail o1, InvoiceDetail o2) {
                if (o1 instanceof ExemptionInvoiceDetail && o2 instanceof ExemptionInvoiceDetail) {
                    ExemptionInvoiceDetail invDet1 = (ExemptionInvoiceDetail) o1;
                    ExemptionInvoiceDetail invDet2 = (ExemptionInvoiceDetail) o2;
                    return invDet1.getPaper().getPaper().getCode().compareTo(invDet2.getPaper().getPaper().getCode());
                }
                if (o1 instanceof RenewalInvoiceDetail && o2 instanceof RenewalInvoiceDetail) {
                    RenewalInvoiceDetail invDet1 = (RenewalInvoiceDetail) o1;
                    RenewalInvoiceDetail invDet2 = (RenewalInvoiceDetail) o2;
                    return invDet1.getYear().compareTo(invDet2.getYear());
                }
                if (o1 instanceof ExamInvoiceDetail && o2 instanceof ExamInvoiceDetail) {
                    ExamInvoiceDetail invDet1 = (ExamInvoiceDetail) o1;
                    ExamInvoiceDetail invDet2 = (ExamInvoiceDetail) o2;
                    if (invDet1.getPaper() != null && invDet2.getPaper() != null) {
                        return invDet1.getPaper().getCode().compareTo(invDet2.getPaper().getCode());
                    }
                    if (invDet1.getSection() != null && invDet2.getSection() != null) {
                        return invDet1.getSection().getSectionPK().getId().compareTo(invDet2.getSection().getSectionPK().getId());
                    }
                    if (invDet1.getLevel() != null && invDet2.getLevel() != null) {
                        return invDet1.getLevel().getLevelPK().getId().compareTo(invDet2.getLevel().getLevelPK().getId());
                    }
                }
                return o2.getLocalAmount().compareTo(o1.getLocalAmount());
            }
        });
        return items;
    }

    public void setItems(List<InvoiceDetail> items) {
        this.items = items;
    }

}
