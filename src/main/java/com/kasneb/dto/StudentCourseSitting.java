/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kasneb.entity.Sitting;

/**
 *
 * @author jikara
 */
public class StudentCourseSitting {

    private Integer id;
    @JsonIgnore
    private Sitting sitting;
    private String sittingDescription;

    public StudentCourseSitting(Integer id, Sitting sitting) {
        this.id = id;
        this.sitting = sitting;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Sitting getSitting() {
        return sitting;
    }

    public void setSitting(Sitting sitting) {
        this.sitting = sitting;
    }

    public String getSittingDescription() {
        if (this.getSitting() != null) {
            sittingDescription = sitting.getSittingDescription();
        }
        return sittingDescription;
    }
}
