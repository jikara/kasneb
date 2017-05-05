/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kasneb.entity.Level;
import com.kasneb.entity.Part;
import java.util.Collection;

/**
 *
 * @author jikara
 */
public class Registration {

    private static final long serialVersionUID = 1L;
    private Integer regNo;
    private String registrationNumber;
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
    private String physicalAddress;
    private String address;
    private String postalCode;
    private String postalCode1;
    private String town;
    private String country;
    private String address1;
    private String town1;
    private String country1;
    private String email;
    private String cellphone;
    private String telephone;
    private Course previousCourse;
    private String learnAbout;
    private LearnAbout learnt;
    private Nation nationality;
    private Qualification qualification;
    @JsonManagedReference
    private Collection<Receipt> receipts;
    private Collection<StudentCoursePaper> eligiblePapers;
    private Collection<ExamBooking> examBookings;
    private ExamEntry examEntry;
    private Collection<Renewal> renewals;
    //core transient
    private Part currentPart;
    private Level currentLevel;
    private String nextRenewal;
    private Collection<Exemption> exemptions;

    public Registration() {
    }

    public Registration(Integer regNo) {
        this.regNo = regNo;
    }

    public Registration(Integer regNo, String registrationNumber, String registered, Integer firstExamDate, String lastName, String firstName, String otherName, String otherName2, Sex sex, String dateOfBirth, String idNumber, Qualification quali, String rrDate, String rrNumber, String pReg, String physicalAddress, String address, String town, String country, String postalCode, String email, String cellphone, String telephone, Course previousCourse, String learnAbout, LearnAbout learnt, Nation nation, Qualification qualification, Collection<Receipt> receipts, Collection<StudentCoursePaper> eligiblePapers, Collection<ExamBooking> examBookings, ExamEntry examEntry) {
        this.regNo = regNo;
        this.registrationNumber = registrationNumber;
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
        this.physicalAddress = physicalAddress;
        this.address = "P.O. Box " + address;
        this.town = town;
        this.country = country;
        this.postalCode = postalCode;
        this.email = email;
        this.cellphone = cellphone;
        this.telephone = telephone;
        this.previousCourse = previousCourse;
        this.learnAbout = learnAbout;
        this.learnt = learnt;
        this.nation = nation;
        this.qualification = qualification;
        this.receipts = receipts;
        this.eligiblePapers = eligiblePapers;
        this.examBookings = examBookings;
        this.examEntry = examEntry;
        //Match
        this.address1 = "P.O. Box " + address;
        this.country1 = country;
        this.town1 = town;
        this.postalCode1 = postalCode;
        this.nationality = nation;
    }

    public Integer getRegNo() {
        return regNo;
    }

    public void setRegNo(Integer regNo) {
        this.regNo = regNo;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
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

    public Nation getNation() {
        return nation;
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
        this.address = "P.O. Box " + address;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getTown1() {
        return town1;
    }

    public void setTown1(String town1) {
        this.town1 = town1;
    }

    public String getCountry1() {
        return country1;
    }

    public void setCountry1(String country1) {
        this.country1 = country1;
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

    public Collection<ExamBooking> getExamBookings() {
        return examBookings;
    }

    public void setExamBookings(Collection<ExamBooking> examBookings) {
        this.examBookings = examBookings;
    }

    public ExamEntry getExamEntry() {
        return examEntry;
    }

    public void setExamEntry(ExamEntry examEntry) {
        this.examEntry = examEntry;
    }

    public Part getCurrentPart() {
        return currentPart;
    }

    public void setCurrentPart(Part currentPart) {
        this.currentPart = currentPart;
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

    public Collection<Exemption> getExemptions() {
        return exemptions;
    }

    public void setExemptions(Collection<Exemption> exemptions) {
        this.exemptions = exemptions;
    }
}
