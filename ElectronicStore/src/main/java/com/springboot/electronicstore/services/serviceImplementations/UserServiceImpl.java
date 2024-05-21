package com.springboot.electronicstore.services.serviceImplementations;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.electronicstore.dtos.UserDto;
import com.springboot.electronicstore.exceptions.customExceptionhandlers.ResourceNotFoundException;
import com.springboot.electronicstore.models.User;
import com.springboot.electronicstore.repositories.UserRepository;
import com.springboot.electronicstore.services.serviceInterfaces.UserService;

// Service implementation for user-related operations
@Service
public class UserServiceImpl implements UserService {

	// Autowiring the UserRepository to perform database operations
	@Autowired
	private UserRepository userRepository;

	// Autowiring the ModelMapper to convert between entity and DTO
	@Autowired
	private ModelMapper mapper;

	// Method to create and save a new user
	@Override
	public UserDto createUser(UserDto userdto) {
		// Generate a unique ID for the new user
		String userid = UUID.randomUUID().toString();
		userdto.setUserId(userid);

		// Convert DTO to entity
		User user = dtoToEntity(userdto);
		User savedUser = userRepository.save(user);

		// Convert entity back to DTO
		UserDto newUserDto = entityToDto(savedUser);

		return newUserDto;
	}

	// Method to update an existing user by their ID
	@Override
	public UserDto updateUser(UserDto userdto, String userId) {
		// Find the existing user or throw an exception if not found
		User foundUser = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("No user found."));

		// Update the user's details
		foundUser.setUserId(userdto.getUserId());
		foundUser.setUserName(userdto.getUserName());
		foundUser.setUserEmail(userdto.getUserEmail());
		foundUser.setUserPassword(userdto.getUserPassword());
		foundUser.setUserGender(userdto.getUserGender());
		foundUser.setUserImage(userdto.getUserImage());

		// Save the updated user entity
		User updatedUser = userRepository.save(foundUser);

		// Convert entity back to DTO
		UserDto updatedUserDto = entityToDto(updatedUser);

		return updatedUserDto;
	}

	// Method to delete an existing user by their ID
	@Override
	public void deleteUser(String userId) {
		// Find the existing user or throw an exception if not found
		User foundUser = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("No user found."));

		// Delete the user entity
		userRepository.delete(foundUser);
	}

	// Method to get a list of all users
	@Override
	public List<UserDto> getAllUsers() {
		// Retrieve all user entities from the repository
		List<User> allUsers = userRepository.findAll();

		// Convert the list of user entities to a list of DTOs
		List<UserDto> allDtoUsers = allUsers.stream().map(user -> entityToDto(user)).collect(Collectors.toList());

		return allDtoUsers;
	}

	// Method to get a user by their ID
	@Override
	public UserDto getUserById(String userId) {
		// Find the user entity by ID or throw an exception if not found
		User foundUser = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("No user found."));

		// Convert entity to DTO
		UserDto foundUserDto = entityToDto(foundUser);

		return foundUserDto;
	}

	// Method to get a user by their email address
	@Override
	public UserDto getUserByEmail(String userEmail) {
		// Find the user entity by email or throw an exception if not found
		User foundUser = userRepository.findByUserEmail(userEmail)
				.orElseThrow(() -> new ResourceNotFoundException("No user found."));

		// Convert entity to DTO
		UserDto foundUserDto = entityToDto(foundUser);

		return foundUserDto;
	}

	// Method to search for users by a keyword in their name
	@Override
	public List<UserDto> searchUsers(String keyword) {
		// Find user entities whose names contain the keyword
		List<User> users = userRepository.findByUserNameContaining(keyword);

		// Convert the list of user entities to a list of DTOs
		List<UserDto> userDtos = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());

		return userDtos;
	}

	// Method to convert an entity to a DTO
	private UserDto entityToDto(User savedUser) {
		return mapper.map(savedUser, UserDto.class);
	}

	// Method to convert a DTO to an entity
	private User dtoToEntity(UserDto userdto) {
		return mapper.map(userdto, User.class);
	}
}
