/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author jikara
 */
public class ElligiblePart {

    private String name;
    private List<ElligibleSection> sections;

    public ElligiblePart() {
    }

    public ElligiblePart(String name, List<ElligibleSection> sections) {
        this.name = name;
        this.sections = sections;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ElligibleSection> getSections() {
        Collections.sort(sections, new Comparator<ElligibleSection>() {
            @Override
            public int compare(ElligibleSection e1, ElligibleSection e2) {
                return e1.getSection().getSectionPK().getId().compareTo(e2.getSection().getSectionPK().getId());
            }
        });
        return sections;
    }

    public void setSections(List<ElligibleSection> sections) {
        this.sections = sections;
    }
}
