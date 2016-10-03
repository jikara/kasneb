/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "role")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @NotNull
    @Basic(optional = false)
    @Column(name = "description", nullable = false, unique = true)
    private String description;
    @OneToMany(mappedBy = "role")
    @JsonBackReference
    private Collection<User> users;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "rolePermission",
            joinColumns = {
                @JoinColumn(
                        name = "roleId",
                        referencedColumnName = "id",
                        nullable = false)},
            inverseJoinColumns = @JoinColumn(
                    name = "permissionCode",
                    referencedColumnName = "code",
                    nullable = false))
    @JsonManagedReference
    private Collection<Permission> permissions;

    public Role() {
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

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public Collection<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<Permission> permissions) {
        this.permissions = permissions;
    }

    public boolean hasPermission(Permission permission) {
        System.out.println(this.permissions.size());
        System.out.println(permission.getCode());
        System.out.println(this.permissions.contains(permission));
        return this.permissions.contains(permission);
    }

}
