package com.springboot.electronicstore.services.serviceInterfaces;

import java.util.List;

import com.springboot.electronicstore.dtos.UserDto;

// UserService interface to define the contract for user-related operations
public interface UserService {

	// Create or save a new user
	UserDto createUser(UserDto userdto);

	// Update an existing user by their ID
	UserDto updateUser(UserDto userdto, String userId);

	// Delete an existing user by their ID
	void deleteUser(String userId);

	// Get a list of all users using pagination and sorting
	List<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

	// Get a user by their ID
	UserDto getUserById(String userId);

	// Get a user by their email address
	UserDto getUserByEmail(String userEmail);

	// Search for users whose details match the specified keyword
	List<UserDto> searchUsers(String keyword);

}
