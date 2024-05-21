package com.springboot.electronicstore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.electronicstore.dtos.UserDto;
import com.springboot.electronicstore.generalMessage.ApiResponseMessage;
import com.springboot.electronicstore.services.serviceInterfaces.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
// Controller class for managing CRUD operations on User entities
public class UserController {

	@Autowired
	private UserService userService;

	// Endpoint to create a new user
	@PostMapping
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
	}

	// Endpoint to update an existing user by their ID
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable String userId) {
		return new ResponseEntity<>(userService.updateUser(userDto, userId), HttpStatus.ACCEPTED);
	}

	// Endpoint to delete an existing user by their ID
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) {
		userService.deleteUser(userId);
		// Return a general success message in JSON format
		ApiResponseMessage message = ApiResponseMessage.builder().message("User is successfully deleted!").success(true)
				.status(HttpStatus.OK).build();
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	// Endpoint to retrieve a list of all users
	@GetMapping
	public ResponseEntity<List<UserDto>> getAllUsers() {
		List<UserDto> users = userService.getAllUsers();
		return new ResponseEntity<>(users, HttpStatus.FOUND);
	}

	// Endpoint to retrieve a user by their ID
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUser(@PathVariable String userId) {
		return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.FOUND);
	}

	// Endpoint to retrieve a user by their email address
	@GetMapping("/email/{userEmail}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable String userEmail) {
		return new ResponseEntity<>(userService.getUserByEmail(userEmail), HttpStatus.FOUND);
	}

	// Endpoint to search for users by a keyword in their name
	@GetMapping("/search/{keyword}")
	public ResponseEntity<List<UserDto>> getUserByKeyword(@PathVariable String keyword) {
		List<UserDto> users = userService.searchUsers(keyword);
		return new ResponseEntity<>(users, HttpStatus.FOUND);
	}

}
