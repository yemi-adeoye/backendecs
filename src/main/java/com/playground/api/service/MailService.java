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
