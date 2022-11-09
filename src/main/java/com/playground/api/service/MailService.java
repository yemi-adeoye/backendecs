package com.playground.api.service;

import java.io.InputStream;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	org.springframework.core.env.Environment env;

	public String sendMessage(String from, String to, String subject, String body) throws Exception {

		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);

		emailSender.send(message);

		return "sent ok";
	}

	public String sendHtmlMessage(String from, String to, String subject, String body) throws Exception {

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, 3);

		// set mail subject
		mimeMessageHelper.setSubject(subject);

		// set sender
		mimeMessageHelper.setFrom(from);

		// set receiver
		mimeMessageHelper.setTo(to);

		String disclaimer = "DISCLAIMER: The content of this email is intended for the person or entity to which it is addressed only. This email may contain confidential information. If you are not the person to whom this message is addressed, be aware that any use, reproduction, or distribution of this message is strictly prohibited. If you received this in error, please contact the sender and immediately delete this email and any attachments.";

		body = "<div style='padding:20px; border: 2px solid blue; color: black; background-color: #eee;'>" + body
				+ "<div style='margin-top:40px;'> <h2 style='color: blue;'>The Incedo Team</h2><small style='text-decoration:none; '>" + disclaimer + "</small>"
				+ "<hr><img src='cid:incedo-footer' /></div></div>";

		mimeMessageHelper.setText(body, true);

		mimeMessageHelper = setCustomIncedoFooter(mimeMessageHelper);

		emailSender.send(message);

		return "sent ok";
	}

	public void sendSignUpMail(String name, String email){
		// send user mail
		String baseUrl = env.getProperty("base_url");
		String link = baseUrl + "login/";
		String fromEmail = env.getProperty("from_email");

		String body = "<strong style='font-size:1.4em;'>Dear " + name + ",</strong>";
		body += "<h2 style='margin-bottom: 0;'>Welcome</h2><hr/>";
		body += "<p style='font-size:1.4em; line-height: 2.5em;'>You've registered successfully. Your access request will be reviewed and you will get a mail granting or denying your request.<br />";
		
		body += "</b><br />If your request is granted you can login by clicking <a href="
				+ link + "> here </a>, or by copying the url below into your browser's address bar.<br/>";
		body += "<code>" + link + "</code></p>";

		// send mail to the user granted access
		try {
			this.sendHtmlMessage(fromEmail, email, "SIGNUP SUCCESS", body);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private MimeMessageHelper setCustomIncedoFooter(MimeMessageHelper helper) {

		ClassPathResource resource;

		try {
			resource = new ClassPathResource("images/footer.gif");
			helper.addInline("incedo-footer", resource);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return helper;

	}
}
