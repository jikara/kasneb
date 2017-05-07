/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "declaration")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Declaration.findAll", query = "SELECT d FROM Declaration d"),
    @NamedQuery(name = "Declaration.findById", query = "SELECT d FROM Declaration d WHERE d.id = :id"),
    @NamedQuery(name = "Declaration.findByDescription", query = "SELECT d FROM Declaration d WHERE d.description = :description")})
public class Declaration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "declaration",fetch = FetchType.LAZY)
    private Collection<StudentDeclaration> studentDeclarations;

    public Declaration() {
    }

    public Declaration(Integer id) {
        this.id = id;
    }

    public Declaration(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<StudentDeclaration> getStudentDeclarations() {
        return studentDeclarations;
    }

    public void setStudentDeclarations(Collection<StudentDeclaration> studentDeclarations) {
        this.studentDeclarations = studentDeclarations;
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
        if (!(object instanceof Declaration)) {
            return false;
        }
        Declaration other = (Declaration) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.kasneb.entity.Declaration[ id=" + id + " ]";
    }

}
