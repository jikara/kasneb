/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

import java.util.Date;
import java.util.List;

/**
 *
 * @author jikara
 */
public class ExamEntry {

    private Integer regNo;
    private Registration cpaRegistration;
    private Integer year;
    private Integer sittingId;
    private Date created;
    private List<ExamPaper> examPapers;

    public Integer getRegNo() {
        return regNo;
    }

    public void setRegNo(Integer regNo) {
        this.regNo = regNo;
    }

    public Registration getCpaRegistration() {
        return cpaRegistration;
    }

    public void setCpaRegistration(Registration cpaRegistration) {
        this.cpaRegistration = cpaRegistration;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSittingId() {
        return sittingId;
    }

    public void setSittingId(Integer sittingId) {
        this.sittingId = sittingId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<ExamPaper> getExamPapers() {
        return examPapers;
    }

    public void setExamPapers(List<ExamPaper> examPapers) {
        this.examPapers = examPapers;
    }
}
