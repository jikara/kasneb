/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "qualificationDocument")
public class QualificationDocument implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name", nullable = false)
    private String name;
    @ManyToOne
    @PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "studentId", referencedColumnName = "studentId"),
        @PrimaryKeyJoinColumn(name = "qualificationId", referencedColumnName = "qualificationId")
    })
    private StudentQualification otherStudentQualification;
    @ManyToOne(optional = false)
    @JoinColumn(name = "studentId", referencedColumnName = "id")
    private Student studentId;
    @ManyToOne(optional = false)
    @JoinColumn(name = "qualificationId", referencedColumnName = "id")
    private Course qualification;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StudentQualification getOtherStudentQualification() {
        return otherStudentQualification;
    }

    public void setOtherStudentQualification(StudentQualification otherStudentQualification) {
        this.otherStudentQualification = otherStudentQualification;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public Course getQualification() {
        return qualification;
    }

    public void setQualification(Course qualification) {
        this.qualification = qualification;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QualificationDocument)) {
            return false;
        }
        QualificationDocument other = (QualificationDocument) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kasneb.api.QualificationDocument[ id=" + id + " ]";
    }

}
