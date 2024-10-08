package com.example.application.security;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Emails {

    private final EmailConfig emailConfig = new EmailConfig();


    public void sendEmail(String toEmail, String subject, String body)throws MessagingException {
        Session session = emailConfig.createSession();

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailConfig.getFromEmail()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }

}
