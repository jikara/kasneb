/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kasneb.entity.pk.StudentQualificationPK;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "studentQualification")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public class StudentQualification implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private StudentQualificationPK studentQualificationPK;
    @ManyToOne(optional = false)
    @JoinColumn(name = "studentId", referencedColumnName = "id", insertable = false, updatable = false)
    
    private Student student;
    @ManyToOne(optional = false)
    @JoinColumn(name = "qualificationId", referencedColumnName = "id", insertable = false, updatable = false)

    private Course qualification;
    @Transient
    protected String type;

    public StudentQualification() {
    }

    public StudentQualification(StudentQualificationPK studentQualificationPK) {
        this.studentQualificationPK = studentQualificationPK;
    }

    public StudentQualificationPK getStudentQualificationPK() {
        return studentQualificationPK;
    }

    public void setStudentQualificationPK(StudentQualificationPK studentQualificationPK) {
        this.studentQualificationPK = studentQualificationPK;
        this.student = new Student(studentQualificationPK.getStudentId());
    }

    public StudentQualification(Student student, Course qualification) {
        this.student = student;
        this.qualification = qualification;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getQualification() {
        return qualification;
    }

    public void setQualification(Course qualification) {
        this.qualification = qualification;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.studentQualificationPK);
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
        final StudentQualification other = (StudentQualification) obj;
        if (!Objects.equals(this.studentQualificationPK, other.studentQualificationPK)) {
            return false;
        }
        return true;
    }

}
