/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.jasper;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author jikara
 */
public class InvoiceDocument {

    private String invoiceNo;
    private String Date;
    private BigDecimal total;
    private List<InvoiceDetail> items;

    public InvoiceDocument() {
    }

    public InvoiceDocument(String invoiceNo, String Date, BigDecimal total, List<InvoiceDetail> items) {
        this.invoiceNo = invoiceNo;
        this.Date = Date;
        this.total = total;
        this.items = items;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
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

    public List<InvoiceDetail> getItems() {
        return items;
    }

    public void setItems(List<InvoiceDetail> items) {
        this.items = items;
    }
}
