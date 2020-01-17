package com.bridgelabz.fundoonotes.service;

import javax.validation.Valid;

import com.bridgelabz.fundoonotes.dto.LoginDetails;
import com.bridgelabz.fundoonotes.dto.ResetPassword;
import com.bridgelabz.fundoonotes.dto.UserDto;
import com.bridgelabz.fundoonotes.model.User;

public interface UserService {

	User registration(UserDto user);

	User login(@Valid LoginDetails loginDetails);

	User verify(String token);

	User resetPassword(String token, ResetPassword resetPassword);

	User forgetPassword(String email);
	
}
