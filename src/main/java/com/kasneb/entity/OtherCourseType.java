/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author jikara
 */
@Entity
@DiscriminatorValue("OTHER")
public class OtherCourseType extends CourseType {

    @Override
    public void setCode(Integer code) {
        super.setCode(code); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer getCode() {
        return super.getCode(); //To change body of generated methods, choose Tools | Templates.
    }


}
