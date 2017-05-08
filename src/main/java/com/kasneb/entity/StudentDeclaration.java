/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "studentDeclaration")
@NamedQueries({
    @NamedQuery(name = "StudentDeclaration.findAll", query = "SELECT s FROM StudentDeclaration s")
    ,    @NamedQuery(name = "StudentDeclaration.findByResponse", query = "SELECT s FROM StudentDeclaration s WHERE s.response = :response")})
public class StudentDeclaration implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private StudentDeclarationPK studentDeclarationPK;
    @Column(name = "response")
    private Boolean response;
    @Lob
    @Size(max = 65535)
    @Column(name = "specification")
    private String specification;    
    @JoinColumn(name = "declarationId", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Declaration declaration;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "studentCourseId", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private StudentCourse studentCourse;
    @JsonInclude
    private transient Student student;

    public StudentDeclaration() {
    }

    public StudentDeclarationPK getStudentDeclarationPK() {
        return studentDeclarationPK;
    }

    public void setStudentDeclarationPK(StudentDeclarationPK studentDeclarationPK) {
        this.studentDeclarationPK = studentDeclarationPK;
    }

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public Declaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }

    public StudentCourse getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(StudentCourse studentCourse) {
        this.studentCourse = studentCourse;
    }

    public Student getStudent() {
        if (getStudentCourse() != null) {
            student = getStudentCourse().getStudentObj();
        }
        return student;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.studentDeclarationPK);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StudentDeclaration other = (StudentDeclaration) obj;
        return Objects.equals(this.studentDeclarationPK, other.studentDeclarationPK);
    }

    @Override
    public String toString() {
        return "StudentDeclaration{" + "studentDeclarationPK=" + studentDeclarationPK + '}';
    }

}
