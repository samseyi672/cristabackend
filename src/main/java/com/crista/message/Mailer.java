package com.crista.message;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class Mailer {

	@Autowired
	JavaMailSender sender;

	@Async
	public void sendMailWithLink(String to, String link,String subject,
			String text,String content ) throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom("samseyi672@gmail.com", "JetLogistics");
		helper.setTo(to);
//		String subject = "here is the  link to reset  your  passord";
//		String content = "<p>Hello</p>" + "<p>you have requested to reset your password</p>"
//				+ "<p>Click the link below to change your password</p>" + "<p><strong><a href=\"" + link
//				+ "\" style=\"color:blue;\">change password</a></strong></p>"
//				+ "<p><b>Please ignore this email if you do remember your password or you have not made the request</b></p>";
		helper.setSubject(subject);
		helper.setText(content, true);
		sender.send(message);
	}
}
