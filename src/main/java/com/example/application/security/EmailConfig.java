package com.example.application.security;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class EmailConfig {

    private final String smtpHost = "smtp.zoho.com";

    // Use TLS
    private final String smtpPort = "587";

    private final String fromEmail = " deskmantra@zohomail.com";
    private final String emailPassword = "NiveaMen@";

    public Properties getMailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // For TLS

        return props;
    }

    public Session createSession() {
        return Session.getInstance(getMailProperties(), new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, emailPassword);
            }
        });
    }

    public String getFromEmail() {
        return fromEmail;
    }
}
