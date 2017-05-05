/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
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
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "studentCourseSitting")
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
    @OneToMany(mappedBy = "studentCourseSitting", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Collection<StudentCourseSittingPaper> papers;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StudentCourseSittingStatus status = StudentCourseSittingStatus.PENDING;
    @Column(name = "synchronized")
    private Boolean newEntry = false;
    @OneToOne
    @JoinColumn(name = "paymentId", referencedColumnName = "id")
    private Payment payment;
    @Transient
    private String fullRegNo;
    @Transient
    private Boolean hasBooking = false;
    @Transient
    private Student student;
    @Column(name = "created", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Nairobi")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date created = new Date();
    @Transient
    private String course;

    public StudentCourseSitting() {
    }

    public StudentCourseSitting(StudentCourse studentCourse, Sitting sitting, ExamCentre sittingCentre, StudentCourseSittingStatus status, Payment payment) {
        this.studentCourse = studentCourse;
        this.sitting = sitting;
        this.sittingCentre = sittingCentre;
        this.status = status;
        this.payment = payment;
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

    public Collection<StudentCourseSittingPaper> getPapers() {
        return papers;
    }

    public void setPapers(Collection<StudentCourseSittingPaper> papers) {
        if (papers != null) {
            for (StudentCourseSittingPaper paper : papers) {
                paper.setStudentCourseSitting(this);
            }
        }
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

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Boolean getNewEntry() {
        return newEntry;
    }

    public void setNewEntry(Boolean newEntry) {
        this.newEntry = newEntry;
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

    public String getFullRegNo() {
        if (getStudentCourse() != null) {
            fullRegNo = getStudentCourse().getFullRegistrationNumber();
        }
        return fullRegNo;
    }

    public String getCourse() {
        if (getStudentCourse() != null) {
            if (getStudentCourse().getCourse() != null) {
                course = getStudentCourse().getCourse().getName();
            }
        }
        return course;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Boolean getHasBooking() {
        if (this.getStatus().equals(StudentCourseSittingStatus.PAID) && this.getSittingCentre() == null) {
            hasBooking = true;
        }
        return hasBooking;
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

    public void addPaper(StudentCourseSittingPaper sittingPaper) {
        if (this.getPapers() != null) {
            this.getPapers().add(sittingPaper);
        }
    }

}
