/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author jikara
 */
public class CpaRegistration implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer regNo;
    private String registrationNumber;
    private Stream stream;
    private Date registered;
    private String lastName;
    private String firstName;
    private String otherName;
    private String otherName2;
    private CsSex sex;
    private Date dateOfBirth;
    private Nation nation;
    private String idNumber;
    private CsQualification quali;
    private Date rrDate;
    private String rrNumber;
    private String pReg;
    private String idNo2;
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String address5;
    private String email;
    private String cellphone;
    private String telephone;
    private Course previousCourse;
    private String learnAbout;
    private LearnAbout learnt;
    private Nation nationality;
    private CsQualification qualification;
    @JsonManagedReference
    private Collection<Receipt> receipts = new ArrayList<>();

    public CpaRegistration() {
    }

    public CpaRegistration(Integer regNo, Stream stream, Date registered, String lastName, String firstName, String otherName, String otherName2, CsSex sex, Date dateOfBirth, Nation nation, String idNumber, CsQualification quali, Date rrDate, String rrNumber, String pReg, String idNo2, String address1, String address2, String address3, String address4, String address5, String email, String cellphone, String telephone, Course previousCourse, String learnAbout, LearnAbout learnt, Nation nationality, CsQualification qualification) {
        this.regNo = regNo;
        this.stream = stream;
        this.registered = registered;
        this.lastName = lastName;
        this.firstName = firstName;
        this.otherName = otherName;
        this.otherName2 = otherName2;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.nation = nation;
        this.idNumber = idNumber;
        this.quali = quali;
        this.rrDate = rrDate;
        this.rrNumber = rrNumber;
        this.pReg = pReg;
        this.idNo2 = idNo2;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.address4 = address4;
        this.address5 = address5;
        this.email = email;
        this.cellphone = cellphone;
        this.telephone = telephone;
        this.previousCourse = previousCourse;
        this.learnAbout = learnAbout;
        this.learnt = learnt;
        this.nationality = nationality;
        this.qualification = qualification;
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

    public Stream getStream() {
        return stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
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

    public CsSex getSex() {
        return sex;
    }

    public void setSex(CsSex sex) {
        this.sex = sex;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Nation getNation() {
        return nation;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
    }

    public Nation getNationality() {
        return nationality;
    }

    public void setNationality(Nation nationality) {
        this.nationality = nationality;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public CsQualification getQualification() {
        return qualification;
    }

    public void setQualification(CsQualification qualification) {
        this.qualification = qualification;
    }

    public Date getRrDate() {
        return rrDate;
    }

    public void setRrDate(Date rrDate) {
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

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getAddress5() {
        return address5;
    }

    public void setAddress5(String address5) {
        this.address5 = address5;
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

    public Collection<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(Collection<Receipt> receipts) {
        this.receipts = receipts;
    }

}
