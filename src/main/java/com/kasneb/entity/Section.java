/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kasneb.entity.pk.SectionPK;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "section", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "partId"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Section.findAll", query = "SELECT s FROM Section s"),
    @NamedQuery(name = "Section.findByName", query = "SELECT s FROM Section s WHERE s.name = :name")})
public class Section implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private SectionPK sectionPK;
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Transient
    private Boolean optional;
    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumns({
        @JoinColumn(name = "partId", referencedColumnName = "id"),
        @JoinColumn(name = "courseId", referencedColumnName = "courseId")
    })
    private Part part;
    @OneToMany(mappedBy = "section")
    private Collection<Paper> paperCollection;
    @OneToMany(mappedBy = "section")
    @JsonBackReference
    private Collection<Fee> feeTypes;
    @OneToMany(mappedBy = "currentSection")
    @JsonBackReference
    private Collection<StudentCourse> studentCourses;

    public Section() {
    }

    public Section(SectionPK sectionPK) {
        this.sectionPK = sectionPK;
    }

    public Section(Integer id, Part part) {
        this.id = id;
        this.part = part;
    }

    public SectionPK getSectionPK() {
        return sectionPK;
    }

    public void setSectionPK(SectionPK sectionPK) {
        this.sectionPK = sectionPK;
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

    public Boolean isOptional() {
        return optional;
    }

    public void setOptional(Boolean optional) {
        this.optional = optional;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    @XmlTransient
    public Collection<Paper> getPaperCollection() {
        return paperCollection;
    }

    public void setPaperCollection(Collection<Paper> paperCollection) {
        this.paperCollection = paperCollection;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.sectionPK);
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
        final Section other = (Section) obj;
        if (!Objects.equals(this.sectionPK, other.sectionPK)) {
            return false;
        }
        return true;
    }

}
