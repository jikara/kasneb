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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author jikara
 */
public class InvoiceDocument {

    private String courseName;
    private String fullName;
    private String registrationNumber;
    private String address;
    private String referenceNumber;
    private String town;
    private String country;
    private String invoiceNumber;
    private String Date;
    private String dueDate;
    private BigDecimal total;
    private String totalString;
    private String currency;
    private List<InvoiceDetail> items;

    public InvoiceDocument() {
    }

    public InvoiceDocument(String courseName, String fullName, String registrationNumber, String address, String referenceNumber, String town, String country, String invoiceNumber, String Date, BigDecimal total, String currency, String dueDate) {
        this.courseName = courseName;
        this.fullName = fullName;
        this.registrationNumber = registrationNumber;
        this.address = address;
        this.referenceNumber = referenceNumber;
        this.town = town;
        this.country = country;
        this.invoiceNumber = invoiceNumber;
        this.Date = Date;
        this.total = total;
        this.currency = currency;
        this.dueDate = dueDate;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
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

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public void addInvoiceDetail(InvoiceDetail item) {
        items = new ArrayList<>();
        items.add(item);
    }

    public String getTotalString() {
        if (total != null) {
            DecimalFormat df = new DecimalFormat("#,###");
            totalString = df.format(total);
        }
        return totalString;
    }
}
