package com.springboot.electronicstore.generalMessage;

import org.springframework.http.HttpStatus;

import lombok.*;

// Lombok annotations for generating getters, setters, constructors, and builder pattern
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponseMessage {

	// Message to describe the API response
	private String message;

	// Indicator of whether the API operation was successful
	private boolean success;

	// HTTP status of the API response
	private HttpStatus status;

}
