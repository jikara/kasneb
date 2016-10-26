/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author jikara
 */
public class CpaPaper implements Serializable {
    private String code;
    private String stream;
    private Integer section;
    private Integer part;
    private String name;
    private Collection<CpaExemption> cpaExemptions;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public Integer getPart() {
        return part;
    }

    public void setPart(Integer part) {
        this.part = part;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<CpaExemption> getCpaExemptions() {
        return cpaExemptions;
    }

    public void setCpaExemptions(Collection<CpaExemption> cpaExemptions) {
        this.cpaExemptions = cpaExemptions;
    }

}
