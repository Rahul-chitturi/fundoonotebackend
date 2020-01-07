package com.bridgelabz.fundoonotes.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bridgelabz.fundoonotes.FundoonotesApplication;

public class Utility {

	private static Logger log = LoggerFactory.getLogger(FundoonotesApplication.class);

	@Autowired
	private JavaMailSender emailSender;

	/**
	 * used to encrypt the password and returns password
	 * 
	 * @param password
	 * @return
	 */
	public static String getEncryPassWord(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		return hashedPassword;
	}

	public static boolean checkPassword(String pass, String newPass) {

		return BCrypt.checkpw(newPass, pass);

	}

	public void sendVerificationEmail(String email, String token) {

		log.info("sending verification Email");
		try {

			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(email);
			msg.setSubject("Testing from Spring Boot");
			msg.setText(token);

			emailSender.send(msg);
			log.info("message sent");
		} catch (Exception e) {
			e.printStackTrace(); // TODO: handle exception
		}
	}
}
