/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kasneb.entity.pk.PartPK;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "part", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "courseId"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Part.findAll", query = "SELECT p FROM Part p"),
    @NamedQuery(name = "Part.findByName", query = "SELECT p FROM Part p WHERE p.name = :name")})
public class Part implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private PartPK partPK;
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @JoinColumn(name = "courseId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonBackReference
    private KasnebCourse course;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "part")
    @JsonManagedReference
    private Collection<Section> sectionCollection;
    @OneToMany(mappedBy = "part")
    private Collection<Paper> paperCollection;
    @OneToMany(mappedBy = "part")
    @JsonBackReference
    private Collection<Fee> feeTypes;
    @OneToMany(mappedBy = "currentPart")
    @JsonBackReference
    private Collection<StudentCourse> studentCourses;

    public Part() {
    }

    public Part(PartPK partPK) {
        this.partPK = partPK;
    }

    public Part(Integer id, KasnebCourse course) {
        this.id = id;
        this.course = course;
    }

    public PartPK getPartPK() {
        return partPK;
    }

    public void setPartPK(PartPK partPK) {
        partPK = new PartPK(getId(), getCourse().getId());
        this.partPK = partPK;
    }

    public Part(Integer id) {
        this.id = id;
    }

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

    public KasnebCourse getCourse() {
        return course;
    }

    public void setCourse(KasnebCourse course) {
        this.course = course;
    }

    public Collection<Section> getSectionCollection() {
        return sectionCollection;
    }

    public void setSectionCollection(Collection<Section> sectionCollection) {
        this.sectionCollection = sectionCollection;
    }

    public Collection<Paper> getPaperCollection() {
        return paperCollection;
    }

    public void setPaperCollection(Collection<Paper> paperCollection) {
        this.paperCollection = paperCollection;
    }

    public Collection<Fee> getFeeTypes() {
        return feeTypes;
    }

    public void setFeeTypes(Collection<Fee> feeTypes) {
        this.feeTypes = feeTypes;
    }

    public Collection<StudentCourse> getStudentCourses() {
        return studentCourses;
    }

    public void setStudentCourses(Collection<StudentCourse> studentCourses) {
        this.studentCourses = studentCourses;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final Part other = (Part) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Part{" + "partPK=" + partPK + ", feeTypes=" + feeTypes + '}';
    }

}
