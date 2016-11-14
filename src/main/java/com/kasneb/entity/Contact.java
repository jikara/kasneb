/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "contact")
@XmlRootElement
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "postalAddress")
    private String postalAddress;
    @Size(max = 45)
    @Column(name = "postalCode")
    private String postalCode;
    @Size(max = 45)
    @Column(name = "town")
    private String town;
    @OneToOne(mappedBy = "contact")
    @JsonBackReference
    private Student student;
    @ManyToOne
    @JoinColumn(name = "countryId", referencedColumnName = "code")
    private Country countryId;
    @ManyToOne(optional = true)
    @JoinColumn(name = "countyId", referencedColumnName = "id")
    @JsonManagedReference
    private County countyId;

    public Contact() {
    }

    public Contact(Integer id) {
        this.id = id;
    }

    public Contact(String postalAddress, String postalCode, String town, Student student, Country countryId, County countyId) {
        this.postalAddress = postalAddress;
        this.postalCode = postalCode;
        this.town = town;
        this.student = student;
        this.countryId = countryId;
        this.countyId = countyId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Country getCountryId() {
        return countryId;
    }

    public void setCountryId(Country countryId) {
        this.countryId = countryId;
    }

    public County getCountyId() {
        return countyId;
    }

    public void setCountyId(County countyId) {
        if (countyId.getId() == null || countyId.getId() == 0) {
            this.countyId = null;
            return;
        }
        this.countyId = countyId;
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
        if (!(object instanceof Contact)) {
            return false;
        }
        Contact other = (Contact) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.kasneb.entity.Contact[ id=" + id + " ]";
    }

}
