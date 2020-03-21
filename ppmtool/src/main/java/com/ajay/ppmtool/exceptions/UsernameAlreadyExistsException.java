package com.ajay.ppmtool.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameAlreadyExistsException extends RuntimeException{

	public UsernameAlreadyExistsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	
	
}
