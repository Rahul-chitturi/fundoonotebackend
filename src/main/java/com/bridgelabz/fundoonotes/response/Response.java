package com.bridgelabz.fundoonotes.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
	private String message;
	private int statusCode;
	private Object obj;
	

	public Response(String message,int statusCode) {
		this.message = message;
		this.statusCode = statusCode;
	}
	

}
