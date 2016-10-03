/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "permission")
public class Permission implements Serializable {

    @Id
    @NotNull
    @Basic(optional = false)
    @Column(name = "code", unique = true, nullable = false)
    private Integer code;
    @Column(name = "description", unique = true, nullable = false)
    private String description;
    @ManyToMany(mappedBy = "permissions")
    @JsonBackReference
    private Collection<Role> roles;

    public Permission() {
    }

    public Permission(Integer code) {
        this.code = code;
    }

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

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.code);
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
        final Permission other = (Permission) obj;
        return Objects.equals(this.code, other.code);
    }

    @Override
    public String toString() {
        return "Permission{" + "code=" + code + ", description=" + description + ", roles=" + roles + '}';
    }

}
