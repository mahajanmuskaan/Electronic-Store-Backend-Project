package com.springboot.electronicstore.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.springboot.electronicstore.exceptions.customExceptionhandlers.BadApiRequest;
import com.springboot.electronicstore.exceptions.customExceptionhandlers.ResourceNotFoundException;
import com.springboot.electronicstore.generalMessage.ApiResponseMessage;

/**
 * Global exception handler for handling exceptions across all REST controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Exception handler for ResourceNotFoundException.
	 * 
	 * @param ex The exception object.
	 * @return ResponseEntity containing the ApiResponseMessage and HTTP status.
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponseMessage> resourceNotFoundException(ResourceNotFoundException ex) {
		// Build an ApiResponseMessage with the exception message and status
		ApiResponseMessage response = ApiResponseMessage.builder().message(ex.getMessage()).status(HttpStatus.NOT_FOUND)
				.success(false).build();
		// Return ResponseEntity with the ApiResponseMessage and HTTP status
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	/**
	 * Exception handler for MethodArgumentNotValidException.
	 * 
	 * @param ex The exception object.
	 * @return ResponseEntity containing the map of field errors and HTTP status.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException ex) {
		List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
		Map<String, Object> response = new HashMap<>();
		allErrors.forEach((error) -> {
			String message = error.getDefaultMessage();
			String field = ((FieldError) error).getField();
			response.put(field, message);
		});
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Exception handler for BadApiRequest.
	 * 
	 * @param ex The exception object.
	 * @return ResponseEntity containing the ApiResponseMessage and HTTP status.
	 */
	@ExceptionHandler(BadApiRequest.class)
	public ResponseEntity<ApiResponseMessage> badApiRequestException(BadApiRequest ex) {
		// Build an ApiResponseMessage with the exception message and status
		ApiResponseMessage response = ApiResponseMessage.builder().message(ex.getMessage())
				.status(HttpStatus.BAD_REQUEST).success(false).build();
		// Return ResponseEntity with the ApiResponseMessage and HTTP status
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
