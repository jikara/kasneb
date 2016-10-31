/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "studentCourseSitting", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"studentCourseId", "sittingId"})})
public class StudentCourseSitting implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "sittingId", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    @JsonManagedReference
    private Sitting sitting;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "invoiceId", unique = true, nullable = true)
    @JsonManagedReference
    private Invoice invoice;
    @ManyToOne
    @JoinColumn(name = "studentCourseId", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private StudentCourse studentCourse;
    @OneToOne
    @JoinColumn(name = "centreId", referencedColumnName = "code", nullable = true)
    @JsonManagedReference
    private ExamCentre sittingCentre;
    @OneToMany(mappedBy = "studentCourseSitting", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<StudentCourseSittingPaper> papers;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StudentCourseSittingStatus status = StudentCourseSittingStatus.PENDING;
    @Transient
    private Student student;

    public StudentCourseSitting() {
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

    public StudentCourse getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(StudentCourse studentCourse) {
        this.studentCourse = studentCourse;
    }

    public Set<StudentCourseSittingPaper> getPapers() {
        return papers;
    }

    public void setPapers(Set<StudentCourseSittingPaper> papers) {
        //get eligible papers
        this.papers = papers;
    }

    public ExamCentre getSittingCentre() {
        return sittingCentre;
    }

    public void setSittingCentre(ExamCentre sittingCentre) {
        this.sittingCentre = sittingCentre;
    }

    public StudentCourseSittingStatus getStatus() {
        return status;
    }

    public void setStatus(StudentCourseSittingStatus status) {
        this.status = status;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public void addInvoice(Invoice invoice) {
        invoice.setStudentCourseSitting(this);
    }

    public void addStudentCourseSittingPaper(StudentCourseSittingPaper paper) {
        if (paper != null) {
            if (papers == null) {
                papers = new HashSet<>();
            }
            papers.add(paper);
            paper.setStudentCourseSitting(this);
        }
    }

    public Student getStudent() {
        if (getStudentCourse() != null) {
            student = getStudentCourse().getStudentObj();
        }
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.id);
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
        final StudentCourseSitting other = (StudentCourseSitting) obj;
        return Objects.equals(this.id, other.id);
    }

}
