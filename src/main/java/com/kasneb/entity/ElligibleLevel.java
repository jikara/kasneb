/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import java.util.Collection;

/**
 *
 * @author jikara
 */
public class ElligibleLevel {

    private String name;
    private Boolean optional = true;
    private Collection<Paper> papers;

    public ElligibleLevel() {
    }

    public ElligibleLevel(String name, Collection<Paper> papers, Boolean optional) {
        this.name = name;
        this.papers = papers;
        this.optional = optional;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Paper> getPapers() {
        return papers;
    }

    public void setPapers(Collection<Paper> papers) {
        this.papers = papers;
    }

    public Boolean getOptional() {
        return optional;
    }

    public void setOptional(Boolean optional) {
        this.optional = optional;
    }
}
