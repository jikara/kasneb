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
public class ElligiblePart {

    private String name;
    private Collection<ElligibleSection> sections;

    public ElligiblePart() {
    }

    public ElligiblePart(String name, Collection<ElligibleSection> sections) {
        this.name = name;
        this.sections = sections;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<ElligibleSection> getSections() {
        return sections;
    }

    public void setSections(Collection<ElligibleSection> sections) {
        this.sections = sections;
    }
}
