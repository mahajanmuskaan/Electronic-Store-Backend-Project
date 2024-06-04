package com.springboot.electronicstore.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Custom validation annotation for validating usernames
@Constraint(validatedBy = UsernameValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {

	// Default error message for invalid username
	String message() default "Invalid username";

	// Groups for constraint validation
	Class<?>[] groups() default {};

	// Payload to provide metadata information about validation
	Class<? extends Payload>[] payload() default {};

}
