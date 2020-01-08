package com.bridgelabz.fundoonotes.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpringEmail {
	@Autowired
	private JavaMailSender emailSender;

	public void sendVerificationEmail(String email, String token) {

		log.info("sending verification Email");
		try {

			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(email);
			msg.setSubject("verify user");
			msg.setText("click here :  "+token);
			emailSender.send(msg);
			log.info("message sent");
		} catch (Exception e) {
			e.printStackTrace(); // TODO: handle exception
		}
	}
	
	public void sendForgetPasswordEmail(String email, String token) {

		log.info("sending verification Email");
		try {

			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(email);
			msg.setSubject("forget password link");
			msg.setText("click here :  "+ token);
			emailSender.send(msg);
			log.info("message sent");
		} catch (Exception e) {
			e.printStackTrace(); // TODO: handle exception
		}
	}
	
}
