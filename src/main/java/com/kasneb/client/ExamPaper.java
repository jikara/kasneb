/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

/**
 *
 * @author jikara
 */
public class ExamPaper {

    private ExamEntry cpaExamEntry;
    private Paper paper;
    private Integer sectionId;
    private String sectionType;

    public ExamEntry getCpaExamEntry() {
        return cpaExamEntry;
    }

    public void setCpaExamEntry(ExamEntry cpaExamEntry) {
        this.cpaExamEntry = cpaExamEntry;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionType() {
        return sectionType;
    }

    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
    }
}
