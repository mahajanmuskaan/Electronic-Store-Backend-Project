package com.springboot.electronicstore.generalMessage;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Lombok annotations for generating getters, setters, constructors, and builder pattern
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponseMessage {

	private String userImage;

	// Message to describe the Image response
	private String message;

	// Indicator of whether the Image operation was successful
	private boolean success;

	// HTTP status of the Image response
	private HttpStatus status;

}
