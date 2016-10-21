/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "county")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "County.findAll", query = "SELECT c FROM County c"),
    @NamedQuery(name = "County.findById", query = "SELECT c FROM County c WHERE c.id = :id"),
    @NamedQuery(name = "County.findByCode", query = "SELECT c FROM County c WHERE c.code = :code"),
    @NamedQuery(name = "County.findByName", query = "SELECT c FROM County c WHERE c.name = :name")})
public class County implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Size(min = 1, max = 5)
    @Column(name = "code", nullable = false)
    private String code;
    @Basic(optional = false)
    @Size(min = 1, max = 45)
    @Column(name = "name", nullable = false)
    private String name;
    @OneToMany(mappedBy = "countyId")
    @JsonBackReference
    private Collection<Student> studentCollection;
    @OneToMany(mappedBy = "countyId")
    @JsonBackReference
    private Collection<Contact> contacts;

    public County() {
    }

    public County(Integer id) {
        this.id = id;
    }

    public County(Integer id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Student> getStudentCollection() {
        return studentCollection;
    }

    public void setStudentCollection(Collection<Student> studentCollection) {
        this.studentCollection = studentCollection;
    }

    public Collection<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Collection<Contact> contacts) {
        this.contacts = contacts;
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
        if (!(object instanceof County)) {
            return false;
        }
        County other = (County) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.kasneb.entity.County[ id=" + id + " ]";
    }

}
