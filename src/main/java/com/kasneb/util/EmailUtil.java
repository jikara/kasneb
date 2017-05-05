/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.util;

import com.kasneb.model.Email;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author jikara
 */
public class EmailUtil {

    static Properties properties;
    static Session session;

    public static void sendEmail(List<Email> emails) throws AddressException, MessagingException {
        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        session = Session.getDefaultInstance(properties, null);
        Transport transport = session.getTransport("smtp");
        transport.connect("smtp.gmail.com", "notification@kasneb.or.ke", "&8h4uMUm");
        for (Email email : emails) {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getAddress()));
            message.setSubject(email.getSubject());
            message.setContent(email.getBody(), "text/html");
            transport.sendMessage(message, message.getAllRecipients());
        }
        transport.close();
    }

}
