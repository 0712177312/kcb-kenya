package com.compulynx.compas.customs;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class SendMail {

	@Autowired
    private JavaMailSender sender;

	public boolean sendEmail(String recipient, String userfname, String password) throws Exception {
		try {
	        MimeMessage message = sender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++Trying to send email+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	        helper.setTo(recipient);
	        helper.setText("<html><body>Hello "+ userfname +" here is your password " + password +"/><body></html>", true);
	       // helper.setText("<html><body>Here is a cat picture! <img src='cid:id101'/>
	        helper.setSubject("Hi");
	        // ClassPathResource file = new ClassPathResource("cat.jpg");
	       // helper.addInline("id101", file);
	        sender.send(message);
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++After sending email+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	        return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }

}
