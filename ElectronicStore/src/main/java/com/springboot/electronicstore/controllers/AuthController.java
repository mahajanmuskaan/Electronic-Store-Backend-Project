package com.springboot.electronicstore.controllers;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.electronicstore.dtos.UserDto;

/**
 * AuthController handles authentication-related endpoints.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private ModelMapper mapper;

	/**
	 * Endpoint to get the current authenticated user's details.
	 * 
	 * @param principal The Principal object representing the currently
	 *                  authenticated user.
	 * @return ResponseEntity containing UserDetails of the current user and HTTP
	 *         status OK.
	 */
	@GetMapping("/currentUser")
	public ResponseEntity<UserDto> getCurrentUser(Principal principal) {

		// Get the username of the currently authenticated user
		String currentUser = principal.getName();

		// Load the user's details using the username and return it in the response
		return new ResponseEntity<>(mapper.map(userDetailsService.loadUserByUsername(currentUser), UserDto.class), HttpStatus.OK);
	}
}
