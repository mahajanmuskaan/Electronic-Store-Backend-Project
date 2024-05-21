package com.springboot.electronicstore.dtos;

import com.springboot.electronicstore.validators.ValidUsername;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

// Lombok annotations to generate getters, setters, no-args constructor, all-args constructor, and builder pattern methods
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    // Unique identifier for the user
    private String userId;

    // Custom annotation to validate the username
    @ValidUsername
    private String userName;

    // Pattern annotation to validate the email format with a custom error message
    @NotBlank
    @Pattern(regexp = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$", message = "Invalid User Email!!")
    private String userEmail;

    // Annotation to ensure the password is not blank
    @NotBlank
    private String userPassword;

    // Annotation to ensure the gender is not blank
    @NotBlank
    private String userGender;

    // User's image URL or path
    private String userImage;
}
