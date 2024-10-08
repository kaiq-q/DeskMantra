package com.example.application.security;
import com.example.application.security.Interfaces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

import static org.atmosphere.annotation.AnnotationUtil.logger;

@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendEmail(String to, String subject, String body)  {
        Emails email = new Emails();
        try {
            email.sendEmail(to, subject, body);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
