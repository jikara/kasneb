/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kasneb.entity.Level;
import com.kasneb.entity.Section;
import java.util.Collection;

/**
 *
 * @author jikara
 */
public class Registration {

    private static final long serialVersionUID = 1L;
    private String regNo;
    private String registrationNumber;
    private Stream stream;
    private String stringStream;
    private String registered;
    private Integer firstExamDate;
    private String lastName;
    private String firstName;
    private String otherName;
    private String otherName2;
    private Sex sex;
    private String dateOfBirth;
    private String dobString;
    private Nation nation;
    private String idNumber;
    private Qualification quali;
    private String rrDate;
    private String rrNumber;
    private String pReg;
    private String idNo2;
    private String address1;
    private String physicalAddress;
    private String address;
    private String town;
    private String country;
    private String email;
    private String cellphone;
    private String telephone;
    private String postalCode;
    private Course previousCourse;
    private String learnAbout;
    private LearnAbout learnt;
    private Nation nationality;
    private Qualification qualification;
    @JsonManagedReference
    private Collection<Receipt> receipts;
    private Collection<StudentCoursePaper> eligiblePapers;
    private Collection<Exemption> exemptions;
    private Collection<ExamBooking> examBookings;
    private ExamEntry cpaExamEntry;
    private Collection<Renewal> renewals;
    //core transient
    private Section currentSection;
    private Level currentLevel;
    private String nextRenewal;

    public Registration() {
    }

    public Registration(String regNo, String registrationNumber, Stream stream, String stringStream, String registered, Integer firstExamDate, String lastName, String firstName, String otherName, String otherName2, Sex sex, String dateOfBirth, String idNumber, Qualification quali, String rrDate, String rrNumber, String pReg, String idNo2, String address1, String physicalAddress, String address, String town, String country, String email, String cellphone, String telephone, Course previousCourse, String learnAbout, LearnAbout learnt, Nation nationality, Qualification qualification, Collection<Receipt> receipts, Collection<StudentCoursePaper> eligiblePapers, Collection<Exemption> exemptions, Collection<ExamBooking> examBookings, ExamEntry cpaExamEntry) {
        this.regNo = regNo;
        this.registrationNumber = registrationNumber;
        this.stream = stream;
        this.stringStream = stringStream;
        this.registered = registered;
        this.firstExamDate = firstExamDate;
        this.lastName = lastName;
        this.firstName = firstName;
        this.otherName = otherName;
        this.otherName2 = otherName2;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.idNumber = idNumber;
        this.quali = quali;
        this.rrDate = rrDate;
        this.rrNumber = rrNumber;
        this.pReg = pReg;
        this.idNo2 = idNo2;
        this.address1 = address1;
        this.physicalAddress = physicalAddress;
        this.address = address;
        this.town = town;
        this.country = country;
        this.email = email;
        this.cellphone = cellphone;
        this.telephone = telephone;
        this.previousCourse = previousCourse;
        this.learnAbout = learnAbout;
        this.learnt = learnt;
        this.nationality = nationality;
        this.qualification = qualification;
        this.receipts = receipts;
        this.eligiblePapers = eligiblePapers;
        this.exemptions = exemptions;
        this.examBookings = examBookings;
        this.cpaExamEntry = cpaExamEntry;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Stream getStream() {
        return stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }

    public String getStringStream() {
        return stringStream;
    }

    public void setStringStream(String stringStream) {
        this.stringStream = stringStream;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public Integer getFirstExamDate() {
        return firstExamDate;
    }

    public void setFirstExamDate(Integer firstExamDate) {
        this.firstExamDate = firstExamDate;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getOtherName2() {
        return otherName2;
    }

    public void setOtherName2(String otherName2) {
        this.otherName2 = otherName2;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Qualification getQuali() {
        return quali;
    }

    public void setQuali(Qualification quali) {
        this.quali = quali;
    }

    public String getRrDate() {
        return rrDate;
    }

    public void setRrDate(String rrDate) {
        this.rrDate = rrDate;
    }

    public String getRrNumber() {
        return rrNumber;
    }

    public void setRrNumber(String rrNumber) {
        this.rrNumber = rrNumber;
    }

    public String getpReg() {
        return pReg;
    }

    public void setpReg(String pReg) {
        this.pReg = pReg;
    }

    public String getIdNo2() {
        return idNo2;
    }

    public void setIdNo2(String idNo2) {
        this.idNo2 = idNo2;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getDobString() {
        return dobString;
    }

    public void setDobString(String dobString) {
        this.dobString = dobString;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Collection<Renewal> getRenewals() {
        return renewals;
    }

    public void setRenewals(Collection<Renewal> renewals) {
        this.renewals = renewals;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Course getPreviousCourse() {
        return previousCourse;
    }

    public void setPreviousCourse(Course previousCourse) {
        this.previousCourse = previousCourse;
    }

    public String getLearnAbout() {
        return learnAbout;
    }

    public void setLearnAbout(String learnAbout) {
        this.learnAbout = learnAbout;
    }

    public LearnAbout getLearnt() {
        return learnt;
    }

    public void setLearnt(LearnAbout learnt) {
        this.learnt = learnt;
    }

    public Nation getNationality() {
        return nationality;
    }

    public void setNationality(Nation nationality) {
        this.nationality = nationality;
    }

    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

    public Collection<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(Collection<Receipt> receipts) {
        this.receipts = receipts;
    }

    public Collection<StudentCoursePaper> getEligiblePapers() {
        return eligiblePapers;
    }

    public void setEligiblePapers(Collection<StudentCoursePaper> eligiblePapers) {
        this.eligiblePapers = eligiblePapers;
    }

    public Collection<Exemption> getExemptions() {
        return exemptions;
    }

    public void setExemptions(Collection<Exemption> exemptions) {
        this.exemptions = exemptions;
    }

    public Collection<ExamBooking> getExamBookings() {
        return examBookings;
    }

    public void setExamBookings(Collection<ExamBooking> examBookings) {
        this.examBookings = examBookings;
    }

    public ExamEntry getCpaExamEntry() {
        return cpaExamEntry;
    }

    public void setCpaExamEntry(ExamEntry cpaExamEntry) {
        this.cpaExamEntry = cpaExamEntry;
    }

    public Section getCurrentSection() {
        return currentSection;
    }

    public void setCurrentSection(Section currentSection) {
        this.currentSection = currentSection;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getNextRenewal() {
        return nextRenewal;
    }
}
