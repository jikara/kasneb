/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.jasper;

import com.kasneb.entity.Invoice;
import java.util.Map;

/**
 *
 * @author jikara
 */
public class ReceiptDocument {

    Map<String, Object> properties;
    Invoice invoice;

    public ReceiptDocument() {
    }

    public ReceiptDocument(Map<String, Object> properties, Invoice invoice) {
        this.properties = properties;
        this.invoice = invoice;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
