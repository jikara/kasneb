/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author jikara
 */
@Entity
@DiscriminatorValue("OTHER")
public class OtherCourseType extends CourseType {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "qualificationId", referencedColumnName = "id")
    private OtherQualification qualification;

    @Override
    public void setCode(Integer code) {
        super.setCode(code); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer getCode() {
        return super.getCode(); //To change body of generated methods, choose Tools | Templates.
    }

    public OtherQualification getQualification() {
        return qualification;
    }

    public void setQualification(OtherQualification qualification) {
        this.qualification = qualification;
    }

}
