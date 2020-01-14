package com.bridgelabz.fundoonotes.servceImplementaion;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.configaration.RabbitMQSender;
import com.bridgelabz.fundoonotes.dto.LoginDetails;
import com.bridgelabz.fundoonotes.dto.ResetPassword;
import com.bridgelabz.fundoonotes.dto.UserDto;
import com.bridgelabz.fundoonotes.model.User;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.response.MailObject;
import com.bridgelabz.fundoonotes.service.UserService;
import com.bridgelabz.fundoonotes.utility.JwtGenerator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImplementation implements UserService {
	/**
	 * 
	 */
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtGenerator tokenGenerator;

	@Autowired
	private RabbitMQSender rabbitMQSender;

	@Override
	public User registration(UserDto user) {

		try {
			User checkEmailAvailability = userRepository.findByEmailAddress(user.getEmail());
			if (checkEmailAvailability == null) {
		
				User userDetails = new User(user.getFirstName(), user.getLastName(), user.getEmail(),
						user.getMobilenumber(), user.getPassword());
				userDetails.setCreatedAt();
				userDetails.setLastLoginTime();
				userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
				userRepository.inserData(userDetails.getCreatedAt(), userDetails.getEmail(), userDetails.getFirstName(),
						userDetails.getLastName(), userDetails.getMobilenumber(), userDetails.getPassword());
				User userDetailtosendMail = userRepository.findByEmailAddress(user.getEmail());
				String response = "http://localhost:8080/users/verify/"
						+ tokenGenerator.jwtToken(userDetailtosendMail.getId());
				MailObject mailObject = new MailObject();
				mailObject.setEmail(userDetailtosendMail.getEmail());
				mailObject.setMessage(response);
				mailObject.setSubject(" user verification");
				rabbitMQSender.send(mailObject);
				//mail.sendVerificationEmail(userDetailtosendMail.getEmail(), response);
				userDetailtosendMail.setPassword("*****");
				return userDetailtosendMail;
			} else {
				return checkEmailAvailability;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public User login(LoginDetails loginDetails) {

		User userInfo = userRepository.findByEmailAddress(loginDetails.getEmail());
		if (userInfo != null) {
			log.info("logining user details : " + userInfo.getEmail());
			if (userInfo.getEmail().equals(loginDetails.getEmail())) {
				if (userInfo.isIs_email_verified()) {
					boolean is_password_matched = BCrypt.checkpw(loginDetails.getPassword(), userInfo.getPassword());
					if (is_password_matched) {
						userRepository.updateLastLoginTime(new Date(), userInfo.getId());
						userInfo.setPassword("****");
						return userInfo;
					} else {
						return null;
					}
				} else {
					User userDetailtosendMail = userRepository.findByEmailAddress(loginDetails.getEmail());
					String response = "http://localhost:8080/users/verify/"
							+ tokenGenerator.jwtToken(userDetailtosendMail.getId());
					MailObject mailObject = new MailObject();
					mailObject.setEmail(userDetailtosendMail.getEmail());
					mailObject.setMessage(response);
					mailObject.setSubject(" user verification");
					rabbitMQSender.send(mailObject);
					
					//mail.sendVerificationEmail(userDetailtosendMail.getEmail(), response);
				}
				return null;
			}
			return null;
		}
		return null;
	}

	@Override
	public User verify(String token) {
		try {
			log.info("id in verification" + (long) tokenGenerator.parseJWT(token));
			Long id = tokenGenerator.parseJWT(token);
			User userInfo = userRepository.findoneById(id);
			if (userInfo != null) {
				if (!userInfo.isIs_email_verified()) {
					userInfo.setIs_email_verified(true);
					userRepository.verify(userInfo.getId());
					return userInfo;
				} else {
					return userInfo;
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public User resetPassword(String token, ResetPassword resetPassword) {

		try {

			if (resetPassword.getPassword().equals(resetPassword.getConformPassword())) {
				long id = tokenGenerator.parseJWT(token);
				User isIdAvailable = userRepository.findoneById(id);
				if (isIdAvailable != null) {
					isIdAvailable.setPassword(passwordEncoder.encode((resetPassword.getPassword())));
					userRepository.save(isIdAvailable);
					return isIdAvailable;
				} else {
					return null;
				}
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public User forgetPassword(String email) {
		User isIdAvailable = userRepository.findByEmailAddress(email);
		if (isIdAvailable != null && isIdAvailable.isIs_email_verified() == true) {
			String response = "http://localhost:8080/users/updatepassword/"
					+ tokenGenerator.jwtToken(isIdAvailable.getId());
			MailObject mailObject = new MailObject();
			mailObject.setEmail(isIdAvailable.getEmail());
			mailObject.setMessage(response);
			mailObject.setSubject("forget password");
			rabbitMQSender.send(mailObject);
			
			//mail.sendForgetPasswordEmail(isIdAvailable.getEmail(), response);
			return isIdAvailable;
		}
		return null;
	}

}
