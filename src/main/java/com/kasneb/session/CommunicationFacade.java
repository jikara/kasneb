/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.AlertType;
import com.kasneb.entity.Communication;
import com.kasneb.entity.Login;
import com.kasneb.entity.Student;
import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.StudentCourseStatus;
import com.kasneb.entity.User;
import com.kasneb.entity.VerificationStatus;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.model.Email;
import com.kasneb.model.Sms;
import com.kasneb.util.Constants;
import com.kasneb.util.GeneratorUtil;
import com.kasneb.util.RestUtil;
import com.kasneb.util.SmsUtil;
import com.kasneb.util.WalletUtil;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

/**
 *
 * @author jikara
 */
@Stateless
public class CommunicationFacade extends AbstractFacade<Communication> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CommunicationFacade() {
        super(Communication.class);
    }

    public List<Integer> findPendingEmails() {
        TypedQuery<Integer> query = em.createQuery("SELECT c.id FROM Communication c WHERE c.alertType =:alertType AND c.status =:status ORDER BY c.id ASC", Integer.class);
        query.setParameter("alertType", AlertType.EMAIL);
        query.setParameter("status", false);
        query.setMaxResults(15);
        return query.getResultList();
    }

    public List<Integer> findPendingSms() {
        TypedQuery<Integer> query = em.createQuery("SELECT c.id FROM Communication c WHERE c.alertType =:alertType AND c.status =:status ORDER BY c.id ASC", Integer.class);
        query.setParameter("alertType", AlertType.SMS);
        query.setParameter("status", false);
        query.setMaxResults(20);
        return query.getResultList();
    }

    @Transactional
    @Schedule(hour = "*", minute = "*", second = "*/39", persistent = false)
    public void sendSms() {
        for (Integer pk : findPendingSms()) {
            Communication communication = super.find(pk);
            try {
                String randomPin = GeneratorUtil.generateRandomPin();
                communication.setPin(randomPin);
                SmsUtil.sendSMS(getSms(communication));
                communication.setStatus(Boolean.TRUE);
                super.edit(communication);
            } catch (IOException | CustomHttpException ex) {
                Logger.getLogger(CommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
//
//    @Transactional
//    @Schedule(hour = "*", minute = "*", second = "*/59", persistent = false)
//    public void sendEmail() {
//        try {
//            List<Email> emails = new ArrayList<>();
//            for (Integer pk : findPendingEmails()) {
//                Communication communication = super.find(pk);
//                try {
//                    emails.add(getEmail(pk));
//                    communication.setStatus(Boolean.TRUE);
//                    super.edit(communication);
//                } catch (IOException | CustomHttpException ex) {
//                    Logger.getLogger(CommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            if (emails.size() > 0) {
//                EmailUtil.sendEmail(emails);
//            }
//        } catch (MessagingException ex) {
//            Logger.getLogger(CommunicationFacade.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    public Email getEmail(Integer pk) throws IOException, CustomHttpException {
        Communication communication = super.find(pk);
        String address = null, body = null, subject = null;
        Login login = null;
        Student student = communication.getStudent();
        User user = communication.getUser();
        if (student != null) {
            login = student.getLoginId();
            String studentName = student.getFirstName() + " " + student.getMiddleName();
        }
        if (user != null) {
            login = user.getLoginId();
        }
        if (login != null) {
            address = login.getEmail();
        }
        switch (communication.getType()) {
            case ACCOUNT_VERIFICATION:
                subject = "Kasneb Account verification";
                body = new RestUtil().getHtml(Constants.BASE_URL + "email/account_verification.xhtml?id=" + communication.getId());
                break;
            case SMS_VERIFICATION:
                subject = "Course Verification";
                body = new RestUtil().getHtml(Constants.BASE_URL + "email/course_verification.xhtml?id=" + communication.getId());
                break;
            case PASSWORD_RESET:
                subject = "Password reset";
                body = new RestUtil().getHtml(Constants.BASE_URL + "email/password_reset.xhtml?id=" + communication.getId());
                break;
            case WALLET_DETAIL:
                subject = "KASNEB Wallet Details";
                body = new RestUtil().getHtml(Constants.BASE_URL + "email/wallet_details.xhtml?id=" + communication.getId());
                break;
            case WALLET_DETAIL_EXISTING:
                subject = "KASNEB Wallet Details";
                body = new RestUtil().getHtml(Constants.BASE_URL + "email/wallet_details_existing.xhtml?id=" + communication.getId());
                break;
            case COURSE_APPLICATION:
                subject = "Course Application";
                body = new RestUtil().getHtml(Constants.BASE_URL + "email/course_application.xhtml?id=" + communication.getId());
                break;
            case COURSE_VERIFICATION:
                subject = "Course Verification";
                body = new RestUtil().getHtml(Constants.BASE_URL + "email/course_verification.xhtml?id=" + communication.getId());
                break;
            case EXAM_APPLICATION:
                subject = "Exam Application";
                body = new RestUtil().getHtml(Constants.BASE_URL + "email/exam_application.xhtml?id=" + communication.getId());
                break;
            case EXEMPTION_APPLICATION:
                subject = "Exemption application";
                body = new RestUtil().getHtml(Constants.BASE_URL + "email/exemption_application.xhtml?id=" + communication.getId());
                break;
            case EXEMPTION_VERIFICATION:
                subject = "Exemption Verification";
                body = new RestUtil().getHtml(Constants.BASE_URL + "email/exemption_verification.xhtml?id=" + communication.getId());
                break;
            case RENEWAL_PAYMENT:
                subject = "Registration Renewal";
                body = new RestUtil().getHtml(Constants.BASE_URL + "email/renewal.xhtml?id=" + communication.getId());
                break;
            case REFUND_REQUEST:
                address = "embwayo@jambopay.com";
                subject = "Refund Request";
                body = new RestUtil().getHtml(Constants.BASE_URL + "email/refund_request.xhtml?id=" + communication.getId());
                break;
            case ADMIN_PASSWORD:
                subject = "Administrator Account";
                body = new RestUtil().getHtml(Constants.BASE_URL + "email/admin_password.xhtml?id=" + communication.getId());
                break;
            case IDENTIFICATION:
                subject = "Indentity verification";
                body = new RestUtil().getHtml(Constants.BASE_URL + "email/identity_verification.xhtml?id=" + communication.getId());
                break;
        }
        return new Email(address, subject, body);
    }

    public Sms getSms(Communication communication) throws CustomHttpException, IOException {
        Student student = communication.getStudent();
        StudentCourse studentCourse = communication.getStudentCourse();
        String studentName = student.getFirstName();
        String body = null;
        switch (communication.getType()) {
            case ACCOUNT_VERIFICATION:
                body = "Dear " + studentName + ",\n"
                        + "Thank you for signing up to the KASNEB student portal. Please login into your email to verify your account.";
                break;
            case SMS_VERIFICATION:
                body = "Your Authentication code for KASNEB is " + student.getLoginId().getSmsToken();
                break;
            case PASSWORD_RESET:
                body = "Your password reset token is " + student.getLoginId().getSmsResetToken();
                break;
            case WALLET_DETAIL:
                String pin = communication.getPin();
                WalletUtil.createWallet(student, pin);
                body = "Dear " + studentName + ",\n"
                        + "Your KASNEB wallet PIN is " + pin + ".Check your email for PIN change instructions.";
                break;
            case WALLET_DETAIL_EXISTING:
                body = "Dear " + studentName + ",\n"
                        + "Your Jambopay(Kasneb) wallet details will be used to make payments.";
                break;
            case COURSE_APPLICATION:
                body = "Dear " + studentName + ",\n"
                        + "Your Registration For " + studentCourse.getCourse().getName() + " was successful.Please wait registration verification.";
                break;
            case COURSE_VERIFICATION:
                if (studentCourse.getVerificationStatus().equals(VerificationStatus.APPROVED)) {
                    body = "Dear " + studentName + ",\n"
                            + "Your Registration for " + studentCourse.getCourse().getName() + " was successful. Your registration number is " + studentCourse.getFullRegistrationNumber() + ". Proceed to book for your examination.";
                } else {
                    body = "Dear " + studentName + ",\n"
                            + "Your Registration for " + studentCourse.getCourse().getName() + " was rejected. For clarifications contact:info@kasneb.or.ke";
                }
                break;
            case EXAM_APPLICATION:
                body = "Dear " + studentName + ",\n"
                        + "Your examination booking for " + studentCourse.getCourse().getName() + " was successful. Your timetable has been sent to your email.";
                break;
            case EXEMPTION_APPLICATION:
                body = "Dear " + studentName + ",\n"
                        + "Thank you for signing up to the KASNEB student portal. Please login into your email to verify your account.";
                break;
            case EXEMPTION_VERIFICATION:
                body = "Dear " + studentName + ",\n"
                        + "Your application for exemption was successful.The exemption letter has been sent to your email. For clarifications contact:info@kasneb.or.ke.";
                break;

            case RENEWAL_PAYMENT:
                body = "Dear " + studentName + ",\n"
                        + "Your renewal payment was successful and your status is now Upto Date.";
                break;

            case IDENTIFICATION:
                if (studentCourse.getCourseStatus().equals(StudentCourseStatus.ACTIVE)) {
                    body = "Dear " + studentName + ",\n"
                            + "We have sucessfully verified your identity and your account is now active.";
                } else {
                    body = "Dear " + studentName + ",\n"
                            + " Your identity verification has been unsuccessful. Please ensure that you attach clear Documents, a valid ID Document and Passport Photo";
                }
                break;

        }
        return new Sms(student.getPhoneNumber(), body);
    }

}
