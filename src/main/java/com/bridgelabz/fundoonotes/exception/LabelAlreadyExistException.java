package com.bridgelabz.fundoonotes.exception;
public class LabelAlreadyExistException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public LabelAlreadyExistException(String message) {
		super(message);
	}
	
}