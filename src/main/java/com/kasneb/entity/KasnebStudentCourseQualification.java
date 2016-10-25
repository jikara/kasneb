/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.kasneb.entity.pk.StudentCourseQualificationPK;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author jikara
 */
@Entity
@DiscriminatorValue(value = "KASNEB")
public class KasnebStudentCourseQualification extends StudentCourseQualification {

    @Override
    public StudentCourseQualificationPK getStudentCourseQualificationPK() {
        return super.getStudentCourseQualificationPK(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setStudentCourseQualificationPK(StudentCourseQualificationPK studentCourseQualificationPK) {
        super.setStudentCourseQualificationPK(studentCourseQualificationPK); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getType() {
        type = "Kasneb";
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Course getQualification() {
        return super.getQualification(); //To change body of generated methods, choose Tools | Templates.
    }

}
