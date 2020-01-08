package com.bridgelabz.fundoonotes.configaration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bridgelabz.fundoonotes.utility.SpringEmail;

@Configuration
public class MailConfig {

	@Bean
	public SpringEmail getMail() {
		return new SpringEmail();
	}
}
