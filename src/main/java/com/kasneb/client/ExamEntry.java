/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

import java.util.List;

/**
 *
 * @author jikara
 */
public class ExamEntry {

    private Integer regNo;
    private Integer partId;
    private Integer sectionId;
    private String sectionType = "N";
    private Integer year;
    private Integer sittingId;
    private Centre centre;
    private String created;
    private String receiptNumber;
    private String flag;
    private Integer status;
    private String papers;
    private String entryPapers;
    private List<ExamPaper> examPapers;
    private List<ExamBooking> examBookings;

    public ExamEntry(Integer regNo, Integer partId, Integer sectionId, Integer year, Integer sittingId, Centre centre, String receiptNumber, String flag, Integer status, String papers, String entryPapers, List<ExamPaper> examPapers, List<ExamBooking> examBookings) {
        this.regNo = regNo;
        this.partId = partId;
        this.sectionId = sectionId;
        this.year = year;
        this.sittingId = sittingId;
        this.centre = centre;
        this.receiptNumber = receiptNumber;
        this.flag = flag;
        this.status = status;
        this.papers = papers;
        this.entryPapers = entryPapers;
        this.examPapers = examPapers;
        this.examBookings = examBookings;
    }

    public Integer getRegNo() {
        return regNo;
    }

    public void setRegNo(Integer regNo) {
        this.regNo = regNo;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getPartId() {
        return partId;
    }

    public void setPartId(Integer partId) {
        this.partId = partId;
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

    public Integer getSittingId() {
        return sittingId;
    }

    public void setSittingId(Integer sittingId) {
        this.sittingId = sittingId;
    }

    public Centre getCentre() {
        return centre;
    }

    public void setCentre(Centre centre) {
        this.centre = centre;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPapers() {
        return papers;
    }

    public void setPapers(String papers) {
        this.papers = papers;
    }

    public String getEntryPapers() {
        return entryPapers;
    }

    public void setEntryPapers(String entryPapers) {
        this.entryPapers = entryPapers;
    }

    public List<ExamPaper> getExamPapers() {
        return examPapers;
    }

    public void setExamPapers(List<ExamPaper> examPapers) {
        this.examPapers = examPapers;
    }

    public List<ExamBooking> getExamBookings() {
        return examBookings;
    }

    public void setExamBookings(List<ExamBooking> examBookings) {
        this.examBookings = examBookings;
    }
}
