/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.kasneb.entity.pk.StudentQualificationPK;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author jikara
 */
@Entity
@DiscriminatorValue(value = "KASNEB")
public class KasnebStudentQualification extends StudentQualification {

    public KasnebStudentQualification() {
    }

    public KasnebStudentQualification(StudentQualificationPK studentCourseQualificationPK) {
        super(studentCourseQualificationPK);
    }

    @Override
    public Student getStudent() {
        return super.getStudent();
    }

    @Override
    public void setStudent(Student student) {
        super.setStudent(student);
    }

    @Override
    public StudentQualificationPK getStudentQualificationPK() {
        return super.getStudentQualificationPK(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setStudentQualificationPK(StudentQualificationPK studentQualificationPK) {
        super.setStudentQualificationPK(studentQualificationPK); //To change body of generated methods, choose Tools | Templates.
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
