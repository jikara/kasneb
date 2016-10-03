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
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "qualificationType")
public class QualificationType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "code")
    private Integer code;
    @Basic(optional = false)
    @Column(name = "description", nullable = false)
    private String description;
    @OneToMany(mappedBy = "type")
    @JsonBackReference
    private Collection<Qualification> qualifications;
    @OneToMany(mappedBy = "type")
    @JsonBackReference
    private Collection<Institution> institutions;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<Qualification> getQualifications() {
        return qualifications;
    }

    public void setQualifications(Collection<Qualification> qualifications) {
        this.qualifications = qualifications;
    }

}
