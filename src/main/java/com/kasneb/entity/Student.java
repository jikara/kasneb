/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kasneb.util.DateUtil;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
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
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s")
    ,    @NamedQuery(name = "Student.findById", query = "SELECT s FROM Student s WHERE s.id = :id")
    ,    @NamedQuery(name = "Student.findByFirstname", query = "SELECT s FROM Student s WHERE s.firstName = :firstName")
    ,    @NamedQuery(name = "Student.findByMiddlename", query = "SELECT s FROM Student s WHERE s.middleName = :middleName")
    ,    @NamedQuery(name = "Student.findByLastname", query = "SELECT s FROM Student s WHERE s.lastName = :lastName")
    ,    @NamedQuery(name = "Student.findByGender", query = "SELECT s FROM Student s WHERE s.gender = :gender")
    ,    @NamedQuery(name = "Student.findByDob", query = "SELECT s FROM Student s WHERE s.dateOfBirth = :dateOfBirth")
    ,    @NamedQuery(name = "Student.findByEmail", query = "SELECT s FROM Student s WHERE s.email = :email")
    ,    @NamedQuery(name = "Student.findByCreated", query = "SELECT s FROM Student s WHERE s.created = :created")
    ,    @NamedQuery(name = "Student.findByPassportPhoto", query = "SELECT s FROM Student s WHERE s.passportPhoto = :passportPhoto")
    ,    @NamedQuery(name = "Student.findByDocumentType", query = "SELECT s FROM Student s WHERE s.documentType = :documentType")
    ,    @NamedQuery(name = "Student.findByDocumentNo", query = "SELECT s FROM Student s WHERE s.documentNo = :documentNo")
    ,    @NamedQuery(name = "Student.findByDocumentScan", query = "SELECT s FROM Student s WHERE s.documentScan = :documentScan")})
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
    @JsonInclude
    private transient String dob;
    @Column(name = "dateOfBirth")
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "dd-MM-yyyy", timezone = "Africa/Nairobi")
    private Date dateOfBirth;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @Column(name = "email", nullable = false)
    private String email;
    @Basic(optional = false)
    @Column(name = "created", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Nairobi")
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
    private Contact contact;
    @JoinColumn(name = "nationality", referencedColumnName = "code")
    @ManyToOne(optional = true)
    private Country nationality;
    @JoinColumn(name = "countryId", referencedColumnName = "code")
    @ManyToOne(optional = true)
    private Country countryId;
    @ManyToOne(optional = true)
    @JoinColumn(name = "countyId", referencedColumnName = "id")
    private County countyId;   
    @OneToOne(mappedBy = "student")
    private Login loginId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student", fetch = FetchType.EAGER)
    private Collection<StudentCourse> studentCourses;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    private Collection<Notification> notifications;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student", targetEntity = StudentQualification.class, fetch = FetchType.LAZY)
    private Set<KasnebStudentQualification> kasnebQualifications;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student", targetEntity = StudentQualification.class, fetch = FetchType.LAZY)
    private Set<OtherStudentQualification> otherQualifications;
    @JsonManagedReference(value = "student-current-course")
    @OneToOne
    @JoinColumn(name = "currentCourseId", referencedColumnName = "id")
    private StudentCourse currentCourse;
    @JsonInclude
    private transient Collection<Invoice> invoices;
    @JsonInclude
    private transient Collection<Invoice> pendingInvoices;
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String jpPin;
    @JsonInclude
    private transient String previousCourseCode;
    @JsonInclude
    private transient Integer previousRegistrationNo;
    @JsonInclude
    private transient String fullName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Transient
    private Integer studentStatus;

    public Student() {
    }

    public Student(Integer id) {
        this.id = id;
    }

    public Student(String firstName, String middleName, String lastName, String phoneNumber, String gender, String email, Country countryId, Date dateOfBirth, String documentNo) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.email = email;
        this.countryId = countryId;
        this.nationality = countryId;
        this.dateOfBirth = dateOfBirth;
        this.documentNo = documentNo;
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
        if (phoneNumber != null) {
            phoneNumber = "254" + phoneNumber.substring(Math.max(phoneNumber.length() - 9, 0));
        }
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

    public String getDob() {
        if (this.getDateOfBirth() != null) {
            try {
                dob = DateUtil.getString(dateOfBirth, "dd-MM-yyyy");
                return dob;
            } catch (ParseException ex) {
                // Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return dob;
    }

    public void setDob(String dob) {
        if (dob != null) {
            try {
                this.dateOfBirth = DateUtil.getDate(dob, "dd-MM-yyyy");
            } catch (ParseException ex) {
                // Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.dob = dob;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public Set<KasnebStudentQualification> getKasnebQualifications() {
        return kasnebQualifications;
    }

    public void setKasnebQualifications(Set<KasnebStudentQualification> kasnebQualifications) {
        for (KasnebStudentQualification kq : kasnebQualifications) {
            kq.setStudent(this);
        }
        this.kasnebQualifications = kasnebQualifications;
    }

    public Set<OtherStudentQualification> getOtherQualifications() {
        return otherQualifications;
    }

    public void setOtherQualifications(Set<OtherStudentQualification> otherQualifications) {
        for (OtherStudentQualification kq : otherQualifications) {
            kq.setStudent(this);
        }
        this.otherQualifications = otherQualifications;
    }

    public Collection<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Collection<Notification> notifications) {
        this.notifications = notifications;
    }

    public Collection<Invoice> getInvoices() {
        invoices = new ArrayList<>();
        if (getStudentCourses() != null) {
            for (StudentCourse studentCourse : getStudentCourses()) {
                if (studentCourse.getInvoices() != null) {
                    invoices.addAll(studentCourse.getInvoices());
                }
            }
        }
        return invoices;
    }

    public Collection<Invoice> getPendingInvoices() {
        pendingInvoices = new ArrayList<>();
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
        return pendingInvoices;
    }

    public String getJpPin() {
        return jpPin;
    }

    public void setJpPin(String jpPin) {
        this.jpPin = jpPin;
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

    public StudentCourse getCurrentCourse() {
        return currentCourse;
    }

    public void setCurrentCourse(StudentCourse currentCourse) {
        this.currentCourse = currentCourse;
    }

    public Integer getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(Integer studentStatus) {
        this.studentStatus = studentStatus;
    }

    public String getFullName() {
        fullName = "";
        if (getFirstName() != null) {
            fullName = fullName + getFirstName();
        }
        if (getMiddleName() != null) {
            fullName = fullName + " " + getMiddleName();
        }
        if (getLastName() != null) {
            fullName = fullName + " " + getLastName();
        }
        return fullName;
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
