/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "student")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s"),
    @NamedQuery(name = "Student.findById", query = "SELECT s FROM Student s WHERE s.id = :id"),
    @NamedQuery(name = "Student.findByFirstname", query = "SELECT s FROM Student s WHERE s.firstName = :firstName"),
    @NamedQuery(name = "Student.findByMiddlename", query = "SELECT s FROM Student s WHERE s.middleName = :middleName"),
    @NamedQuery(name = "Student.findByLastname", query = "SELECT s FROM Student s WHERE s.lastName = :lastName"),
    @NamedQuery(name = "Student.findByGender", query = "SELECT s FROM Student s WHERE s.gender = :gender"),
    @NamedQuery(name = "Student.findByDob", query = "SELECT s FROM Student s WHERE s.dob = :dob"),
    @NamedQuery(name = "Student.findByEmail", query = "SELECT s FROM Student s WHERE s.email = :email"),
    @NamedQuery(name = "Student.findByCreated", query = "SELECT s FROM Student s WHERE s.created = :created"),
    @NamedQuery(name = "Student.findByPassportPhoto", query = "SELECT s FROM Student s WHERE s.passportPhoto = :passportPhoto"),
    @NamedQuery(name = "Student.findByDocumentType", query = "SELECT s FROM Student s WHERE s.documentType = :documentType"),
    @NamedQuery(name = "Student.findByDocumentNo", query = "SELECT s FROM Student s WHERE s.documentNo = :documentNo"),
    @NamedQuery(name = "Student.findByDocumentScan", query = "SELECT s FROM Student s WHERE s.documentScan = :documentScan")})
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "firstName", nullable = false)
    private String firstName;
    @Column(name = "middleName")
    private String middleName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;
    @Basic(optional = false)
    @Column(name = "gender", nullable = true)
    private String gender;
    @Column(name = "dateOfBirth")
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dob;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @Column(name = "email", nullable = false)
    private String email;
    @Basic(optional = false)
    @Column(name = "created", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date created = new Date();
    @Column(name = "passportPhoto")
    private String passportPhoto;
    @ManyToOne
    @JoinColumn(name = "documentType", referencedColumnName = "id")
    private DocumentType documentType;
    @Column(name = "documentNo")
    private String documentNo;
    @Column(name = "documentScan")
    private String documentScan;
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JsonManagedReference
    private Contact contact;
    @JoinColumn(name = "nationality", referencedColumnName = "code")
    @ManyToOne(optional = true)
    @JsonManagedReference
    private Country nationality;
    @JoinColumn(name = "countryId", referencedColumnName = "code")
    @ManyToOne(optional = true)
    @JsonManagedReference
    private Country countryId;
    @ManyToOne(optional = true)
    @JoinColumn(name = "countyId", referencedColumnName = "id")
    @JsonManagedReference
    private County countyId;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "student")
    @JsonBackReference
    private Login loginId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Collection<StudentCourse> studentCourses;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    @JsonManagedReference
    private Collection<Notification> notifications;
    @Transient
    private StudentCourse currentCourse;
    @Transient
    private Collection<Invoice> invoices = new ArrayList<>();
    @Transient
    private Collection<Invoice> pendingInvoices = new ArrayList<>();
    @Transient
    @JsonIgnore
    private String jpPin;
    @Transient
    private String previousCourseCode;
    @Transient
    private Integer previousRegistrationNo;
    @Transient
    private Integer studentStatus;

    public Student() {
    }

    public Student(Integer id) {
        this.id = id;
    }

    public Student(String firstName, String middleName, String lastName, String phoneNumber, String gender, String email, Country countryId, Date dob) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.email = email;
        this.countryId = countryId;
        this.nationality = countryId;
        this.dob = dob;
    }

    public Student(String firstName, String middleName, String lastName, String phoneNumber, String email) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getPassportPhoto() {
        return passportPhoto;
    }

    public void setPassportPhoto(String passportPhoto) {
        this.passportPhoto = passportPhoto;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public String getDocumentScan() {
        return documentScan;
    }

    public void setDocumentScan(String documentScan) {
        this.documentScan = documentScan;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    public Country getCountryId() {
        return countryId;
    }

    public void setCountryId(Country countryId) {
        this.countryId = countryId;
    }

    public County getCountyId() {
        return countyId;
    }

    public void setCountyId(County countyId) {
        this.countyId = countyId;
    }

    public Login getLoginId() {
        return loginId;
    }

    public void setLoginId(Login loginId) {
        this.loginId = loginId;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Collection<StudentCourse> getStudentCourses() {
        return studentCourses;
    }

    public void setStudentCourses(Collection<StudentCourse> studentCourses) {
        if (studentCourses != null) {
            for (StudentCourse sc : studentCourses) {
                sc.setStudent(this);
            }
        }
        this.studentCourses = studentCourses;
    }

    public Collection<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Collection<Notification> notifications) {
        this.notifications = notifications;
    }

    public Collection<Invoice> getInvoices() {
        try {
            if (getStudentCourses() != null) {
                for (StudentCourse studentCourse : getStudentCourses()) {
                    if (studentCourse != null) {
                        invoices.addAll(studentCourse.getInvoices());
                    }
                }
            }
        } catch (Exception e) {

        }
        return invoices;
    }

    public Collection<Invoice> getPendingInvoices() {
        try {
            if (getStudentCourses() != null) {
                for (StudentCourse studentCourse : getStudentCourses()) {
                    if (studentCourse.getInvoices() != null) {
                        for (Invoice invoice : studentCourse.getInvoices()) {
                            if (invoice.getStatus().getStatus().equals("PENDING")) {
                                pendingInvoices.add(invoice);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
        return pendingInvoices;
    }

    public String getJpPin() {
        return jpPin;
    }

    public void setJpPin(String jpPin) {
        this.jpPin = jpPin;
    }

    public StudentCourse getCurrentCourse() {
        if (getStudentCourses() != null) {
            for (StudentCourse course : getStudentCourses()) {
                if (course.getActive()) {
                    return course;
                }
            }
        }
        return currentCourse;
    }

    public void setCurrentCourse(StudentCourse currentCourse) {
        this.currentCourse = currentCourse;
    }

    public String getPreviousCourseCode() {

        return previousCourseCode;
    }

    public void setPreviousCourseCode(String previousCourseCode) {
        try {
            int code = Integer.parseInt(previousCourseCode);
            if (code < 10) {
                previousCourseCode = "0" + code;
            }
        } catch (Exception e) {

        }
        this.previousCourseCode = previousCourseCode;
    }

    public Integer getPreviousRegistrationNo() {
        return previousRegistrationNo;
    }

    public void setPreviousRegistrationNo(Integer previousRegistrationNo) {
        this.previousRegistrationNo = previousRegistrationNo;
    }

    public Integer getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(Integer studentStatus) {
        this.studentStatus = studentStatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName + ", email=" + email + '}';
    }

}
