package com.compulynx.compas.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String recipient, String subject, String emailContent) {

        System.out.println(javaMailSender);

        System.out.println(recipient);
        System.out.println(subject);
        System.out.println(emailContent);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("michaelmbugua.me@gmail.com");
        msg.setTo(recipient);

        msg.setSubject(subject);
        msg.setText(emailContent);

        this.javaMailSender.send(msg);
    }
}
