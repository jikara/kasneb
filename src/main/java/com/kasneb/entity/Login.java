/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "login")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Login.findAll", query = "SELECT l FROM Login l")
    ,    @NamedQuery(name = "Login.findById", query = "SELECT l FROM Login l WHERE l.id = :id")
    ,    @NamedQuery(name = "Login.findByEmail", query = "SELECT l FROM Login l WHERE l.email = :email")
    ,    @NamedQuery(name = "Login.findByPassword", query = "SELECT l FROM Login l WHERE l.password = :password")
    ,    @NamedQuery(name = "Login.findByVerificationToken", query = "SELECT l FROM Login l WHERE l.verificationToken = :verificationToken")
    ,    @NamedQuery(name = "Login.findByLoginAttempts", query = "SELECT l FROM Login l WHERE l.loginAttempts = :loginAttempts")
    ,    @NamedQuery(name = "Login.emailActivated", query = "SELECT l FROM Login l WHERE l.emailActivated = :emailActivated")
    ,    @NamedQuery(name = "Login.phoneNumberActivated", query = "SELECT l FROM Login l WHERE l.phoneNumberActivated = :phoneNumberActivated")
    ,    @NamedQuery(name = "Login.findByBanned", query = "SELECT l FROM Login l WHERE l.banned = :banned")})
public class Login implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @Column(name = "email", nullable = false)
    private String email;
    @Basic(optional = false)
    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Basic(optional = false)
    @Column(name = "password", nullable = false)
    private String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "verificationToken")
    private String verificationToken;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "smsToken")
    private String smsToken;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "resetToken")
    private String resetToken;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "smsResetToken")
    private String smsResetToken;
    @Basic(optional = false)
    @Column(name = "loginAttempts", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int loginAttempts = 0;
    @Basic(optional = false)
    @Column(name = "emailActivated", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean emailActivated = false;
    @Basic(optional = false)
    @Column(name = "phoneNumberActivated", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean phoneNumberActivated = false;
    @Basic(optional = false)
    @Column(name = "banned", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean banned = false;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private transient Date lastLogin;
    @OneToOne(optional = true)
    @JoinColumn(name = "studentId", referencedColumnName = "id", nullable = true)
    private Student student;
    @JsonManagedReference(value = "user-login")
    @OneToOne(optional = true)
    @JoinColumn(name = "userId", referencedColumnName = "id", nullable = true)
    private User user;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Transient
    private Integer status;

    public Login() {
    }

    public Login(Integer id) {
        this.id = id;
    }

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Login(Integer id, String email, String password, int loginAttempts, boolean emailActivated, boolean phoneNumberActivated, boolean banned, Date lastLogin) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.loginAttempts = loginAttempts;
        this.emailActivated = emailActivated;
        this.phoneNumberActivated = phoneNumberActivated;
        this.banned = banned;
        this.lastLogin = lastLogin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public String getSmsToken() {
        return smsToken;
    }

    public void setSmsToken(String smsToken) {
        this.smsToken = smsToken;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getSmsResetToken() {
        return smsResetToken;
    }

    public void setSmsResetToken(String smsResetToken) {
        this.smsResetToken = smsResetToken;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isEmailActivated() {
        return emailActivated;
    }

    public void setEmailActivated(boolean emailActivated) {
        this.emailActivated = emailActivated;
    }

    public boolean isPhoneNumberActivated() {
        return phoneNumberActivated;
    }

    public void setPhoneNumberActivated(boolean phoneNumberActivated) {
        this.phoneNumberActivated = phoneNumberActivated;
    }

    public boolean getBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        if (student != null) {
            this.phoneNumber = student.getPhoneNumber();
        }
        this.student = student;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getStatus() {
        status = 200;
        if (getUser() != null) {
            if (!getUser().getPasswordChanged()) {
                status = 500;
            }
        }
        return status;
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
        if (!(object instanceof Login)) {
            return false;
        }
        Login other = (Login) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.kasneb.entity.Login[ id=" + id + " ]";
    }

}
