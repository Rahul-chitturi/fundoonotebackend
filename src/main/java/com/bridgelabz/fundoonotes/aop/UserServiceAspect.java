package com.bridgelabz.fundoonotes.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoonotes.dto.UserDto;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class UserServiceAspect {

	@Before(value = "execution(* com.bridgelabz.fundoonotes.servceImplementaion.UserServiceImplementation.*(..)) and args(user)")
	public void beforeRegistration(JoinPoint joinPoint, UserDto user) {
		log.info("Before method:" + joinPoint.getSignature());
		log.info("Creating User registration name: " + user.getFirstName() + " " + user.getLastName());

	}

	@After(value = "execution(* com.bridgelabz.fundoonotes.servceImplementaion.UserServiceImplementation.*(..)) and args(user)")
	public void afterRegistration(JoinPoint joinPoint, UserDto user) {

		log.info("After method:" + joinPoint.getSignature());
		log.info("Successfully response returned ");

	}

	@Before(value = "execution(* com.bridgelabz.fundoonotes.servceImplementaion.UserServiceImplementation.*(..)) and args(token)")
	public void beforeVerify(JoinPoint joinPoint, String token) {
		log.info("Before method:" + joinPoint.getSignature());
		log.info("verifying the user");
	}

	@AfterReturning(value = "execution(* com.bridgelabz.fundoonotes.servceImplementaion.UserServiceImplementation.*(..))", returning = "result")
	public void afterVerify(JoinPoint joinPoint, boolean result) {
		log.info("Before method:" + joinPoint.getSignature());
		log.info("returned " + result);
	}

	@AfterThrowing(pointcut = "execution(* com.bridgelabz.fundoonotes.servceImplementaion.UserServiceImplementation.*(..))", throwing = "ex")
	public void AfterThrowingAdvice(IllegalArgumentException ex) {
		log.info("There has been an exception: " + ex.toString());
	}

}
