/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author jikara
 */
@Embeddable
public class StudentDeclarationPK implements Serializable {

    @Column(name = "declarationId")
    private Integer declarationId;
    @Column(name = "studentCourseId")
    private Integer studentCourseId;

    public StudentDeclarationPK() {
    }

    public StudentDeclarationPK(Integer declarationId, Integer studentCourseId) {
        this.declarationId = declarationId;
        this.studentCourseId = studentCourseId;
    }

    public Integer getDeclarationId() {
        return declarationId;
    }

    public void setDeclarationId(Integer declarationId) {
        this.declarationId = declarationId;
    }

    public Integer getStudentCourseId() {
        return studentCourseId;
    }

    public void setStudentCourseId(Integer studentCourseId) {
        this.studentCourseId = studentCourseId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.declarationId);
        hash = 97 * hash + Objects.hashCode(this.studentCourseId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StudentDeclarationPK)) {
            return false;
        }
        StudentDeclarationPK pk = (StudentDeclarationPK) obj;
        return Objects.equals(this.declarationId, pk.declarationId) && pk.studentCourseId.equals(this.studentCourseId);
    }

    @Override
    public String toString() {
        return "StudentDeclarationPK{" + "declarationId=" + declarationId + ", studentCourseId=" + studentCourseId + '}';
    }
}
