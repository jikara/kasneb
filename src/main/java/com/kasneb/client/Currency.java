/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

/**
 *
 * @author jikara
 */
public class Currency {

    private String code;
    private String name;
    private String symbol;
    private Boolean defaultCurrency;

    public Currency(String code) {
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Boolean getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(Boolean defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }
}
