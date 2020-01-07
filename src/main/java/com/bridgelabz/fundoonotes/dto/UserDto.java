package com.bridgelabz.fundoonotes.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDto {
    
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;

	@Email
	@NotNull
	private String email;
	
    @NotNull
	private long mobilenumber;
	
    @NotNull
	private String password;
	

}
