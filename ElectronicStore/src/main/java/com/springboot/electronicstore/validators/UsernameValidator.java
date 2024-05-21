package com.springboot.electronicstore.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// Implementation of ConstraintValidator for ValidUsername annotation
public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

	// Initializes the validator in preparation for isValid calls
	@Override
	public void initialize(ValidUsername constraintAnnotation) {
		// No initialization needed
	}

	// Validates the value against the constraints defined in ValidUsername
	// annotation
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// Check if the value is blank
		if (value.isBlank()) {
			return false;
		}
		return true;
	}

}
