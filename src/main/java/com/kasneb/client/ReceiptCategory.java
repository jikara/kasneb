/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

import com.kasneb.entity.FeeCode;

/**
 *
 * @author jikara
 */
public class ReceiptCategory {

    private String code;
    private String name;
    private FeeCode feeCode;

    public ReceiptCategory(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FeeCode getFeeCode() {
        return feeCode;
    }

    public void setFeeCode(FeeCode feeCode) {
        this.feeCode = feeCode;
    }
}
