/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "studentCourseSittingPaper", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"studentCourseSittingId", "paperCode"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudentCourseSittingPaper.findAll", query = "SELECT s FROM StudentCourseSittingPaper s"),
    @NamedQuery(name = "StudentCourseSittingPaper.findById", query = "SELECT s FROM StudentCourseSittingPaper s WHERE s.paperCode = :paperCode")})
public class StudentCourseSittingPaper implements Serializable {

    @Id
    @Column(name = "paperCode")
    private String paperCode;
    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    @Column(name = "paperStatus", nullable = false)
    private PaperStatus paperStatus = PaperStatus.PENDING;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paperCode", referencedColumnName = "code", nullable = false, insertable = false, updatable = false)
    @JsonManagedReference
    private Paper paper;
    @ManyToOne(optional = false)
    @JoinColumn(name = "studentCourseSittingId", referencedColumnName = "id", nullable = true)
    @JsonBackReference
    private StudentCourseSitting studentCourseSitting;

    public StudentCourseSittingPaper() {
    }

    public String getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }

    public PaperStatus getPaperStatus() {
        return paperStatus;
    }

    public void setPaperStatus(PaperStatus paperStatus) {
        this.paperStatus = paperStatus;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public StudentCourseSitting getStudentCourseSitting() {
        return studentCourseSitting;
    }

    public void setStudentCourseSitting(StudentCourseSitting studentCourseSitting) {
        this.studentCourseSitting = studentCourseSitting;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.paperCode);
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
        final StudentCourseSittingPaper other = (StudentCourseSittingPaper) obj;
        return Objects.equals(this.paperCode, other.paperCode);
    }

}
