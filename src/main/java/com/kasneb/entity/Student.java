/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
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
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

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

    @Size(min = 1, max = 45)
    @Column(name = "firstName", nullable = false)
    private String firstName;
    @Size(max = 45)
    @Column(name = "middleName")
    private String middleName;
    @Size(max = 45)
    @Column(name = "lastName")
    private String lastName;
    @Size(max = 45)
    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;
    @Basic(optional = false)
    @Size(min = 1, max = 45)
    @Column(name = "gender", nullable = true)
    private String gender;
    @Past
    @Column(name = "dateOfBirth")
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dob = new Date();
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)

    @Size(min = 1, max = 45)
    @Column(name = "email", nullable = false)
    private String email;
    @Basic(optional = false)

    @Column(name = "created", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created = new Date();
    @Size(max = 45)
    @Column(name = "passportPhoto")
    private String passportPhoto;
    @Size(max = 45)
    @Column(name = "documentType")
    private String documentType;
    @Size(max = 45)
    @Column(name = "documentNo")
    private String documentNo;
    @Size(max = 45)
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
    @JoinColumn(name = "countyId", referencedColumnName = "id")
    @ManyToOne
    @JsonManagedReference
    private County countyId;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "student")
    @JsonBackReference
    private Login loginId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    @JsonManagedReference
    private Collection<StudentCourse> studentCourses;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    @JsonManagedReference
    private Collection<Notification> notifications;
    @OneToMany(mappedBy = "student")
    @JsonManagedReference
    private Collection<Invoice> invoices;

    public Student() {
    }

    public Student(Integer id) {
        this.id = id;
    }

    public Student(Integer id, String firstName, String gender, Date dob, String email, Date created) {
        this.id = id;
        this.firstName = firstName;
        this.gender = gender;
        this.dob = dob;
        this.email = email;
        this.created = created;
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

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
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

    @XmlTransient
    public Collection<StudentCourse> getStudentCourses() {
        return studentCourses;
    }

    public void setStudentCourses(Collection<StudentCourse> studentCourses) {
        this.studentCourses = studentCourses;
    }

    public Collection<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Collection<Notification> notifications) {
        this.notifications = notifications;
    }

    public Collection<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Collection<Invoice> invoices) {
        this.invoices = invoices;
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
