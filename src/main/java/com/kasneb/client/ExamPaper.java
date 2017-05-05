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

    private ExamPaperPK pk;
    private ExamEntry examEntry;
    private Paper paper;
    private Integer sectionId;
    private Integer levelId;
    private String sectionType;

    public ExamPaper() {
    }

    public ExamPaper(ExamPaperPK pk, Integer sectionId, Integer levelId, String sectionType) {
        this.pk = pk;
        this.sectionId = sectionId;
        this.levelId = levelId;
        this.sectionType = sectionType;
    }

    public ExamPaperPK getPk() {
        return pk;
    }

    public void setPk(ExamPaperPK pk) {
        this.pk = pk;
    }

    public ExamEntry getExamEntry() {
        return examEntry;
    }

    public void setExamEntry(ExamEntry examEntry) {
        this.examEntry = examEntry;
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

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public String getSectionType() {
        return sectionType;
    }

    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
    }

    public class ExamPaperPK {

        private Integer regNo;
        private String paperCode;

        public ExamPaperPK(Integer regNo, String paperCode) {
            this.regNo = regNo;
            this.paperCode = paperCode;
        }

        public Integer getRegNo() {
            return regNo;
        }

        public void setRegNo(Integer regNo) {
            this.regNo = regNo;
        }

        public String getPaperCode() {
            return paperCode;
        }

        public void setPaperCode(String paperCode) {
            this.paperCode = paperCode;
        }
    }
}
