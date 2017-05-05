/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.dto;

import java.util.Date;

/**
 *
 * @author jikara
 */
public class Exemption {

    private Integer id;
    private String reference;
    private Date date;

    public Exemption(Integer id, String reference, Date date) {
        this.id = id;
        this.reference = reference;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
