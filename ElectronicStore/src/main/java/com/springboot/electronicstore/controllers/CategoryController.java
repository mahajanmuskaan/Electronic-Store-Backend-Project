package com.springboot.electronicstore.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.electronicstore.dtos.CategoryDto;
import com.springboot.electronicstore.dtos.UserDto;
import com.springboot.electronicstore.generalMessage.ApiResponseMessage;
import com.springboot.electronicstore.generalMessage.ImageResponseMessage;
import com.springboot.electronicstore.services.serviceInterfaces.CategoryService;
import com.springboot.electronicstore.services.serviceInterfaces.FileService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// REST Controller to handle category-related HTTP requests
@RestController
@RequestMapping("/category")
public class CategoryController {

    // Autowire the CategoryService to perform category operations
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
	private FileService fileService;

	@Value("${category.profile.image.path}")
	private String fileUploadPath;

    // Create a new category
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto category) {
        CategoryDto createdCategory = categoryService.createCategory(category);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    // Update an existing category by its ID
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto category,
                                                      @PathVariable String categoryId) {
        CategoryDto updatedCategory = categoryService.updateCategory(category, categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.ACCEPTED);
    }

    // Delete a category by its ID
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategory(categoryId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Category is successfully deleted!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // Get all categories with pagination and sorting
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "categoryTitle", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        List<CategoryDto> categories = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(categories, HttpStatus.FOUND);
    }

    // Get a category by its ID
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String categoryId) {
        return new ResponseEntity<>(categoryService.getCategory(categoryId), HttpStatus.FOUND);
    }
    
    @PostMapping("/image/{categoryId}")
	public ResponseEntity<ImageResponseMessage> uploadUserImage(@PathVariable String categoryId,
			@RequestParam("categoryImage") MultipartFile categoryImage) throws IOException {

		// Upload the category image file
		String categoryImageName = fileService.uploadFile(categoryImage, fileUploadPath);

		// Update the user's image name in the database
		CategoryDto category = categoryService.getCategory(categoryId);
		category.setCategoryImage(categoryImageName);
		categoryService.updateCategory(category, categoryId);

		// Build the image upload response message
		ImageResponseMessage imageResponse = ImageResponseMessage.builder().userImage(categoryImageName)
				.message("Image Name is updated!").success(true).status(HttpStatus.OK).build();

		return new ResponseEntity<>(imageResponse, HttpStatus.OK);
	}


	@GetMapping(value = "/image/{categoryId}", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
	public void serveUserImageFile(@PathVariable String categoryId, HttpServletResponse response) throws IOException {

		// Retrieve the category's image file
		CategoryDto category = categoryService.getCategory(categoryId);
		InputStream resource = fileService.getResource(fileUploadPath, category.getCategoryImage());

		// Set the response content type as image/jpeg or image/png
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		response.setHeader("Content-Disposition", "inline; filename=" + category.getCategoryImage());

		// Copy the image input stream to the response output stream
		StreamUtils.copy(resource, response.getOutputStream());
	}
}
