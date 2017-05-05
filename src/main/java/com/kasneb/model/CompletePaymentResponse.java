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
public class CompletePaymentResponse {

    private String BusinessName;
    private String InvoiceNumber;
    private String MerchantCode;
    private String Names;
    private String OrderID;
    private String Pin;
    private String ID;
    private String ReceiptNumber;
    private String LocalReceiptNumber;
    private String ReferenceNumber;
    private int PaymentTypeID;
    private int Amount;
    private String TransactionID;
    private String MerchantID;
    private String PhoneNumber;

    public CompletePaymentResponse() {
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String BusinessName) {
        this.BusinessName = BusinessName;
    }

    public String getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(String InvoiceNumber) {
        this.InvoiceNumber = InvoiceNumber;
    }

    public String getMerchantCode() {
        return MerchantCode;
    }

    public void setMerchantCode(String MerchantCode) {
        this.MerchantCode = MerchantCode;
    }

    public String getNames() {
        return Names;
    }

    public void setNames(String Names) {
        this.Names = Names;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String OrderID) {
        this.OrderID = OrderID;
    }

    public String getPin() {
        return Pin;
    }

    public void setPin(String Pin) {
        this.Pin = Pin;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getReceiptNumber() {
        return ReceiptNumber;
    }

    public void setReceiptNumber(String ReceiptNumber) {
        this.ReceiptNumber = ReceiptNumber;
    }

    public String getLocalReceiptNumber() {
        return LocalReceiptNumber;
    }

    public void setLocalReceiptNumber(String LocalReceiptNumber) {
        this.LocalReceiptNumber = LocalReceiptNumber;
    }

    public String getReferenceNumber() {
        return ReferenceNumber;
    }

    public void setReferenceNumber(String ReferenceNumber) {
        this.ReferenceNumber = ReferenceNumber;
    }

    public int getPaymentTypeID() {
        return PaymentTypeID;
    }

    public void setPaymentTypeID(int PaymentTypeID) {
        this.PaymentTypeID = PaymentTypeID;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int Amount) {
        this.Amount = Amount;
    }

    public String getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID(String TransactionID) {
        this.TransactionID = TransactionID;
    }

    public String getMerchantID() {
        return MerchantID;
    }

    public void setMerchantID(String MerchantID) {
        this.MerchantID = MerchantID;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }
}
