package com.springboot.electronicstore.exceptions.customExceptionhandlers;

public class BadApiRequest extends RuntimeException{

	public BadApiRequest() {
		super("Bad Request!");
	}
	
	public BadApiRequest(String message) {
		super(message);
	}

}
