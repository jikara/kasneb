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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "country")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Country.findAll", query = "SELECT c FROM Country c"),
    @NamedQuery(name = "Country.findByCode", query = "SELECT c FROM Country c WHERE c.code = :code"),
    @NamedQuery(name = "Country.findByName", query = "SELECT c FROM Country c WHERE c.name = :name"),
    @NamedQuery(name = "Country.findByNationality", query = "SELECT c FROM Country c WHERE c.nationality = :nationality")})
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "code", nullable = false)
    private String code;
    @Basic(optional = false)
    @Column(name = "name", nullable = false)
    private String name;
    @Basic(optional = false)
    @Column(name = "nationality", nullable = false)
    private String nationality;
    @Basic(optional = false)
    @Column(name = "phoneCode")
    private int phoneCode;
    @JsonBackReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nationality")
    private Collection<Student> studentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "countryId")
    @JsonBackReference
    private Collection<Student> studentCollection1;
    @OneToMany(mappedBy = "countryId")
    private Collection<Contact> contacts;

    public Country() {
    }

    public Country(String code) {
        this.code = code;
    }

    public Country(String code, String name, String nationality) {
        this.code = code;
        this.name = name;
        this.nationality = nationality;
    }

    public Country(String code, String name, String nationality, int phoneCode) {
        this.code = code;
        this.name = name;
        this.nationality = nationality;
        this.phoneCode = phoneCode;
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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(int phoneCode) {
        this.phoneCode = phoneCode;
    }

    public Collection<Student> getStudentCollection() {
        return studentCollection;
    }

    public void setStudentCollection(Collection<Student> studentCollection) {
        this.studentCollection = studentCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Country)) {
            return false;
        }
        Country other = (Country) object;
        return !((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code)));
    }

    @Override
    public String toString() {
        return "com.kasneb.entity.Country[ id=" + code + " ]";
    }

}
