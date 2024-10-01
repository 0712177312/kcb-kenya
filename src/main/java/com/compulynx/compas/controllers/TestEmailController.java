package com.compulynx.compas.controllers;

import com.compulynx.compas.customs.Api;
import com.compulynx.compas.mail.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = Api.REST)
public class TestEmailController {
    @Autowired
    private EmailSender emailSender;
    @GetMapping("/sendEmail")
    public String sendEmail() throws Exception {
        try {
            System.out.println("Trying to send email.............................................................####");

            String subject = "Biometric Details of Customer Captured";
            String emailContent = "Dear " + "Michael" + ", your biometric details have been successfully registered. For any queries please call 0711087000 or 0732187000.";
            String recipient = "michaelmbugua.me@gmail.com";

            emailSender.sendEmail(recipient, subject, emailContent);
            System.out.println("Email to staff scheduled to be sent to " + recipient);

            return "Email Sent";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Email not sent";
    }
}