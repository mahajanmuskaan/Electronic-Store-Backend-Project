package com.springboot.electronicstore.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.electronicstore.dtos.UserDto;
import com.springboot.electronicstore.generalMessage.ApiResponseMessage;
import com.springboot.electronicstore.generalMessage.ImageResponseMessage;
import com.springboot.electronicstore.services.serviceInterfaces.FileService;
import com.springboot.electronicstore.services.serviceInterfaces.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
// Controller class for managing CRUD operations on User entities
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;

	@Value("${user.profile.image.path}")
	private String fileUploadPath;

	/**
	 * Endpoint to create a new user.
	 * 
	 * @param userDto The UserDto object containing user information.
	 * @return ResponseEntity with the created UserDto and HTTP status CREATED.
	 */
	@PostMapping
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
	}

	/**
	 * Endpoint to update an existing user by their ID.
	 * 
	 * @param userDto The UserDto object containing updated user information.
	 * @param userId  The ID of the user to be updated.
	 * @return ResponseEntity with the updated UserDto and HTTP status ACCEPTED.
	 */
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable String userId) {
		return new ResponseEntity<>(userService.updateUser(userDto, userId), HttpStatus.ACCEPTED);
	}

	/**
	 * Endpoint to delete an existing user by their ID.
	 * 
	 * @param userId The ID of the user to be deleted.
	 * @return ResponseEntity with a general success message and HTTP status OK.
	 */
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) {
		userService.deleteUser(userId);
		ApiResponseMessage message = ApiResponseMessage.builder().message("User is successfully deleted!").success(true)
				.status(HttpStatus.OK).build();
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	/**
	 * Endpoint to retrieve a list of all users with pagination and sorting.
	 * 
	 * @param pageNumber The page number for pagination.
	 * @param pageSize   The number of users per page.
	 * @param sortBy     The field to sort by.
	 * @param sortDir    The sort direction ('asc' or 'desc').
	 * @return ResponseEntity with a list of UserDto objects and HTTP status FOUND.
	 */
	@GetMapping
	public ResponseEntity<List<UserDto>> getAllUsers(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "userName", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
		List<UserDto> users = userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<>(users, HttpStatus.FOUND);
	}

	/**
	 * Endpoint to retrieve a user by their ID.
	 * 
	 * @param userId The ID of the user to retrieve.
	 * @return ResponseEntity with the UserDto object and HTTP status FOUND.
	 */
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUser(@PathVariable String userId) {
		return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.FOUND);
	}

	/**
	 * Endpoint to retrieve a user by their email address.
	 * 
	 * @param userEmail The email address of the user to retrieve.
	 * @return ResponseEntity with the UserDto object and HTTP status FOUND.
	 */
	@GetMapping("/email/{userEmail}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable String userEmail) {
		return new ResponseEntity<>(userService.getUserByEmail(userEmail), HttpStatus.FOUND);
	}

	/**
	 * Endpoint to search for users by a keyword in their name.
	 * 
	 * @param keyword The keyword to search for in user names.
	 * @return ResponseEntity with a list of UserDto objects matching the keyword and
	 *         HTTP status FOUND.
	 */
	@GetMapping("/search/{keyword}")
	public ResponseEntity<List<UserDto>> getUserByKeyword(@PathVariable String keyword) {
		List<UserDto> users = userService.searchUsers(keyword);
		return new ResponseEntity<>(users, HttpStatus.FOUND);
	}

	/**
	 * Endpoint to upload a user's image file.
	 * 
	 * @param userId    The ID of the user whose image is to be updated.
	 * @param userImage The MultipartFile containing the user's image.
	 * @return ResponseEntity with an ImageResponseMessage and HTTP status OK.
	 * @throws IOException If an I/O error occurs.
	 */
	@PostMapping("/image/{userId}")
	public ResponseEntity<ImageResponseMessage> uploadUserImage(@PathVariable String userId,
			@RequestParam("userImage") MultipartFile userImage) throws IOException {

		// Upload the user image file
		String userImageName = fileService.uploadFile(userImage, fileUploadPath);

		// Update the user's image name in the database
		UserDto user = userService.getUserById(userId);
		user.setUserImage(userImageName);
		userService.updateUser(user, userId);

		// Build the image upload response message
		ImageResponseMessage imageResponse = ImageResponseMessage.builder().userImage(userImageName)
				.message("Image Name is updated!").success(true).status(HttpStatus.OK).build();

		return new ResponseEntity<>(imageResponse, HttpStatus.OK);
	}

	/**
	 * Endpoint to serve a user's image file.
	 * 
	 * @param userId   The ID of the user whose image is to be retrieved.
	 * @param response The HttpServletResponse used to return the image data.
	 * @throws IOException If an I/O error occurs.
	 */
	@GetMapping(value = "/image/{userId}", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
	public void serveUserImageFile(@PathVariable String userId, HttpServletResponse response) throws IOException {

		// Retrieve the user's image file
		UserDto user = userService.getUserById(userId);
		InputStream resource = fileService.getResource(fileUploadPath, user.getUserImage());

		// Set the response content type as image/jpeg or image/png
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		response.setHeader("Content-Disposition", "inline; filename=" + user.getUserImage());

		// Copy the image input stream to the response output stream
		StreamUtils.copy(resource, response.getOutputStream());
	}

}
