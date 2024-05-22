package com.springboot.electronicstore.services.serviceImplementations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.springboot.electronicstore.dtos.UserDto;
import com.springboot.electronicstore.exceptions.customExceptionhandlers.ResourceNotFoundException;
import com.springboot.electronicstore.models.User;
import com.springboot.electronicstore.repositories.UserRepository;
import com.springboot.electronicstore.services.serviceInterfaces.UserService;

/**
 * Service implementation for user-related operations.
 */
@Service
public class UserServiceImpl implements UserService {

	@Value("${user.profile.image.path}")
	private String relativePath;

	// Autowiring the UserRepository to perform database operations
	@Autowired
	private UserRepository userRepository;

	// Autowiring the ModelMapper to convert between entity and DTO
	@Autowired
	private ModelMapper mapper;

	/**
	 * Method to create and save a new user.
	 * 
	 * @param userdto The UserDto object containing user information.
	 * @return The created UserDto.
	 */
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

	/**
	 * Method to update an existing user by their ID.
	 * 
	 * @param userdto The UserDto object containing updated user information.
	 * @param userId  The ID of the user to be updated.
	 * @return The updated UserDto.
	 */
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

	/**
	 * Method to delete an existing user by their ID.
	 * 
	 * @param userId The ID of the user to be deleted.
	 */
	@Override
	public void deleteUser(String userId) {
		// Find the existing user or throw an exception if not found
		User foundUser = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("No user found."));

		// Get full path of image folder and concatenate with image filename
		Path directoryPath = Paths.get(System.getProperty("user.dir"), relativePath);
		String fullpath = directoryPath + File.separator + foundUser.getUserImage();
		Path imagePath = Paths.get(fullpath);

		// Delete User Image file
		try {
			Files.delete(imagePath);
		} catch (NoSuchFileException ex) {
			// Handle case where file to delete is missing
			ex.printStackTrace();
		} catch (IOException e) {
			// Handle other I/O errors
			e.printStackTrace();
		}

		// Delete the user entity
		userRepository.delete(foundUser);
	}

	/**
	 * Method to get a list of all users with pagination and sorting.
	 * 
	 * @param pageNumber The page number for pagination.
	 * @param pageSize   The number of users per page.
	 * @param sortBy     The field to sort by.
	 * @param sortDir    The sort direction ('asc' or 'desc').
	 * @return A list of UserDto objects.
	 */
	@Override
	public List<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// Determine the sort direction
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		// Create a Pageable object with the page number, page size, and sort direction
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		// Retrieve the paginated list of user entities from the repository
		Page<User> page = userRepository.findAll(pageable);
		List<User> allUsers = page.getContent();

		// Convert the list of user entities to a list of DTOs
		List<UserDto> allDtoUsers = allUsers.stream().map(this::entityToDto).collect(Collectors.toList());

		return allDtoUsers;
	}

	/**
	 * Method to get a user by their ID.
	 * 
	 * @param userId The ID of the user to retrieve.
	 * @return The UserDto object.
	 */
	@Override
	public UserDto getUserById(String userId) {
		// Find the user entity by ID or throw an exception if not found
		User foundUser = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("No user found."));

		// Convert entity to DTO
		UserDto foundUserDto = entityToDto(foundUser);

		return foundUserDto;
	}

	/**
	 * Method to get a user by their email address.
	 * 
	 * @param userEmail The email address of the user to retrieve.
	 * @return The UserDto object.
	 */
	@Override
	public UserDto getUserByEmail(String userEmail) {
		// Find the user entity by email or throw an exception if not found
		User foundUser = userRepository.findByUserEmail(userEmail)
				.orElseThrow(() -> new ResourceNotFoundException("No user found."));

		// Convert entity to DTO
		UserDto foundUserDto = entityToDto(foundUser);

		return foundUserDto;
	}

	/**
	 * Method to search for users by a keyword in their name.
	 * 
	 * @param keyword The keyword to search for in user names.
	 * @return A list of UserDto objects.
	 */
	@Override
	public List<UserDto> searchUsers(String keyword) {
		// Find user entities whose names contain the keyword
		List<User> users = userRepository.findByUserNameContaining(keyword);

		// Convert the list of user entities to a list of DTOs
		List<UserDto> userDtos = users.stream().map(this::entityToDto).collect(Collectors.toList());

		return userDtos;
	}

	/**
	 * Method to convert an entity to a DTO.
	 * 
	 * @param savedUser The User entity object.
	 * @return The corresponding UserDto object.
	 */
	private UserDto entityToDto(User savedUser) {
		return mapper.map(savedUser, UserDto.class);
	}

	/**
	 * Method to convert a DTO to an entity.
	 * 
	 * @param userdto The UserDto object.
	 * @return The corresponding User entity object.
	 */
	private User dtoToEntity(UserDto userdto) {
		return mapper.map(userdto, User.class);
	}
}
