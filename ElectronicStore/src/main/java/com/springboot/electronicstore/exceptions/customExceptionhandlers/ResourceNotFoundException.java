package com.springboot.electronicstore.exceptions.customExceptionhandlers;

// Custom exception class for resource not found scenarios
public class ResourceNotFoundException extends RuntimeException {

	// Default constructor
	public ResourceNotFoundException() {
		super();
	}

	// Constructor with a message parameter
	public ResourceNotFoundException(String message) {
		super(message);
	}

}
