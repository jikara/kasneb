/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "sitting", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"sittingYear", "sittingPeriod"})})
@XmlRootElement
public class Sitting implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "registrationDeadline", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Nairobi")
    private Date registrationDeadline;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "examEntryDeadline", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Nairobi")
    private Date examEntryDeadline;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "lateExamEntryDeadline", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Nairobi")
    private Date lateExamEntryDeadline;
    @Basic(optional = false)
    @Column(name = "sittingPeriod")
    @Enumerated(EnumType.STRING)
    private SittingPeriod sittingPeriod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sittingYear")
    private Integer sittingYear;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "status")
    private String status;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "lateRegistrationDeadline", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Nairobi")
    private Date lateRegistrationDeadline;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sitting")

    private Collection<StudentCourseSitting> studentCourseSittings;
    @OneToMany(mappedBy = "firstSitting")

    private List<StudentCourse> studentCourses;
    @OneToMany(mappedBy = "sitting")
    private Collection<SittingPaper> sittingPapers;
    @Transient
    private Integer partYear;
    @Transient
    private String sittingDescription;

    public Sitting() {
    }

    public Sitting(Integer sittingYear, Integer partYear) {
        this.sittingYear = sittingYear;
        switch (partYear) {
            case 1:
                this.sittingPeriod = SittingPeriod.MAY;
                break;
            case 2:
                this.sittingPeriod = SittingPeriod.NOVEMBER;
                break;
        }
    }

    public Integer getSittingYear() {
        return sittingYear;
    }

    public void setSittingYear(Integer sittingYear) {
        this.sittingYear = sittingYear;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Sitting(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SittingPeriod getSittingPeriod() {
        return sittingPeriod;
    }

    public void setSittingPeriod(SittingPeriod sittingPeriod) {
        this.sittingPeriod = sittingPeriod;
    }

    public String getSittingDescription() {
        if (getSittingPeriod() != null && getSittingYear() != null) {
            sittingDescription = getSittingPeriod().name() + " " + getSittingYear();
        }
        return sittingDescription;
    }

    public Date getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(Date registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public Date getLateRegistrationDeadline() {
        return lateRegistrationDeadline;
    }

    public void setLateRegistrationDeadline(Date lateRegistrationDeadline) {
        this.lateRegistrationDeadline = lateRegistrationDeadline;
    }

    public Date getExamEntryDeadline() {
        return examEntryDeadline;
    }

    public void setExamEntryDeadline(Date examEntryDeadline) {
        this.examEntryDeadline = examEntryDeadline;
    }

    public Date getLateExamEntryDeadline() {
        return lateExamEntryDeadline;
    }

    public void setLateExamEntryDeadline(Date lateExamEntryDeadline) {
        this.lateExamEntryDeadline = lateExamEntryDeadline;
    }

    public Collection<StudentCourseSitting> getStudentCourseSittings() {
        return studentCourseSittings;
    }

    public void setStudentCourseSittings(Collection<StudentCourseSitting> studentCourseSittings) {
        this.studentCourseSittings = studentCourseSittings;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final Sitting other = (Sitting) obj;
        return Objects.equals(this.id, other.id);
    }

}
