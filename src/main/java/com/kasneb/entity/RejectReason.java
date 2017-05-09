/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

/**
 *
 * @author jikara
 */
public enum RejectReason {
    OK(""), REJ_1("REJ_1"), REJ_2("REJ_2"), REJ_3("REJ_3"), REJ_4("REJ_4"), REJ_5("REJ_5");
    private final String formatted;

    RejectReason(String formatted) {
        this.formatted = formatted;
    }

    @Override
    public String toString() {
        return formatted;
    }
}
