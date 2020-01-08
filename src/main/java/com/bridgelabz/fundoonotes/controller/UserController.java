package com.bridgelabz.fundoonotes.controller;

/**
 * user controller 
 * @author user
 * @date : 07/01/2020
 * @version :1.0
 */
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bridgelabz.fundoonotes.dto.LoginDetails;
import com.bridgelabz.fundoonotes.dto.ResetPassword;
import com.bridgelabz.fundoonotes.dto.UserDto;
import com.bridgelabz.fundoonotes.model.User;
import com.bridgelabz.fundoonotes.response.Response;
import com.bridgelabz.fundoonotes.response.UserAuthenticationResponse;
import com.bridgelabz.fundoonotes.service.UserService;
import com.bridgelabz.fundoonotes.utility.JwtGenerator;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtGenerator tokenGenerator;

	/**
	 * api for user registration
	 * 
	 * @param userDto
	 * @param bindingResult
	 * @return response
	 */
	@PostMapping(value = "/register")
	private ResponseEntity<Response> registration(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Response(bindingResult.getAllErrors().get(0).getDefaultMessage(), 400, null));
		} else {
			User user = userService.registration(userDto);
			userDto.setPassword("****");
			return user != null
					? ResponseEntity.status(HttpStatus.CREATED)
							.body(new Response("registration successfull", 200, user))
					: ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
							.body(new Response("user already exist", 400, userDto));
		}
	}

	/**
	 * api for user login
	 * 
	 * @param loginDetails
	 * @param bindingResult
	 * @return response
	 */
	@PostMapping(value = "/login")
	@CachePut(key = "#token", value = "userId")
	private ResponseEntity<UserAuthenticationResponse> login(@Valid @RequestBody LoginDetails loginDetails,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			loginDetails.setPassword("****");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserAuthenticationResponse(
					bindingResult.getAllErrors().get(0).getDefaultMessage(), 400, loginDetails));
		}
		User userInformation = userService.login(loginDetails);
loginDetails.setPassword("*******");
		return userInformation != null
				? ResponseEntity.status(HttpStatus.OK)
						.body(new UserAuthenticationResponse(tokenGenerator.jwtToken(userInformation.getId()), 200,
								userInformation))
				: ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new UserAuthenticationResponse("Login failed", 400, loginDetails));
	}

	/**
	 * api for user verification
	 * 
	 * @param token
	 * @return response Entity
	 */
	@PutMapping("/verify/{token}")
	@CachePut(key = "#token", value = "userId")
	private ResponseEntity<Response> userVerification(@PathVariable("token") String token) {
		try {
			User user = userService.verify(token);
			return user != null ? ResponseEntity.status(HttpStatus.OK).body(new Response("verified", 200))
					: ResponseEntity.status(HttpStatus.OK).body(new Response("not verified", 400));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * api for reset password
	 * 
	 * @param token
	 * @param pswd
	 * @return response
	 * @throws Exception
	 */

	@PostMapping("/resetpassword/{token}")
	@CachePut(key = "#token", value = "userId")
	public ResponseEntity<Response> resetPassword(@PathVariable("token") String token,
			@RequestBody ResetPassword resetPassword) {
		User user = userService.resetPassword(token, resetPassword);

		return user != null
				? ResponseEntity.status(HttpStatus.OK).body(new Response("Password is Update Successfully", 200))
				: ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new Response("Password and Confirm Password doesn't matched", 400));

	}

	/**
	 * api for forget password
	 * 
	 * @param email
	 * @return response
	 */
	@PostMapping("/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestParam("email") String email) {

		User user = userService.forgetPassword(email);
		if (user != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("User Exist", 200));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("User Doesn't Exist", 400));
		}
	}
}
