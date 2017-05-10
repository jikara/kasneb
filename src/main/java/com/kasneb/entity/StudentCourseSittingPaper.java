/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kasneb.entity.pk.StudentCourseSittingPaperPK;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "studentCourseSittingPaper")
@XmlRootElement
public class StudentCourseSittingPaper implements Serializable {

    @EmbeddedId
    private StudentCourseSittingPaperPK pk;
    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    @Column(name = "paperStatus", nullable = false)
    private PaperStatus paperStatus;
    @ManyToOne(optional = false)
    @JoinColumn(name = "paperCode", referencedColumnName = "code", updatable = false, insertable = false)
    private Paper paper;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotFound(action = NotFoundAction.IGNORE)    
    @ManyToOne(optional = false,fetch=FetchType.EAGER)
    @JoinColumn(name = "studentCourseSittingId", referencedColumnName = "id", updatable = false, insertable = false)
    private StudentCourseSitting studentCourseSitting;     
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  
    private transient String paperCode;

    public StudentCourseSittingPaper() {
    }

    public StudentCourseSittingPaper(Paper paper, PaperStatus paperStatus, StudentCourseSitting studentCourseSitting) {
        this.paper = paper;
        this.paperStatus = paperStatus;
        this.studentCourseSitting = studentCourseSitting;
    }

    public StudentCourseSittingPaperPK getPk() {
        return pk;
    }

    public void setPk(StudentCourseSittingPaperPK pk) {
        this.pk = pk;
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

    public String getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.pk);
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
        if (!Objects.equals(this.pk, other.pk)) {
            return false;
        }
        return true;
    }

    

}
