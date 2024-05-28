package com.springboot.electronicstore.services.serviceImplementations;

import java.io.File;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.electronicstore.dtos.CategoryDto;
import com.springboot.electronicstore.exceptions.customExceptionhandlers.ResourceNotFoundException;
import com.springboot.electronicstore.models.Category;
import com.springboot.electronicstore.repositories.CategoryRepository;
import com.springboot.electronicstore.services.serviceInterfaces.CategoryService;

// Service implementation for category-related operations
@Service
public class CategoryServiceImpl implements CategoryService {

	// Inject the relative path for storing category images from application
	// properties
	@Value("${category.profile.image.path}")
	private String relativePath;

	// Autowire the CategoryRepository to perform database operations
	@Autowired
	private CategoryRepository categoryRepo;

	// Autowire the ModelMapper to convert between entity and DTO
	@Autowired
	private ModelMapper mapper;

	// Method to create and save a new category
	@Override
	public CategoryDto createCategory(CategoryDto category) {
		// Generate a unique ID for the new category
		String categoryId = UUID.randomUUID().toString();
		category.setCategoryId(categoryId);

		// Convert DTO to entity
		Category categoryEntity = mapper.map(category, Category.class);
		Category savedCategory = categoryRepo.save(categoryEntity);

		// Convert entity back to DTO
		return mapper.map(savedCategory, CategoryDto.class);
	}

	// Method to update an existing category by its ID
	@Override
	public CategoryDto updateCategory(CategoryDto category, String categoryId) {
		// Find the existing category or throw an exception if not found
		Category foundCategory = categoryRepo.findById(categoryId).orElseThrow(
				() -> new ResourceNotFoundException("Category with the given id: " + categoryId + " not found!!"));

		// Update the category's details
		foundCategory.setCategoryId(category.getCategoryId());
		foundCategory.setCategoryTitle(category.getCategoryTitle());
		foundCategory.setCategoryDescription(category.getCategoryDescription());
		foundCategory.setCategoryImage(category.getCategoryImage());

		// Save the updated category entity
		Category updatedCategory = categoryRepo.save(foundCategory);

		// Convert entity back to DTO
		return mapper.map(updatedCategory, CategoryDto.class);
	}

	// Method to delete an existing category by its ID
	@Override
	public void deleteCategory(String categoryId) {
		// Find the existing category or throw an exception if not found
		Category foundCategory = categoryRepo.findById(categoryId).orElseThrow(
				() -> new ResourceNotFoundException("Category with the given id: " + categoryId + " not found!!"));

		// Get full path of image folder and concatenate with image filename
		Path directoryPath = Paths.get(System.getProperty("user.dir"), relativePath);
		String fullpath = directoryPath + File.separator + foundCategory.getCategoryImage();
		Path imagePath = Paths.get(fullpath);

		// Delete category image file
		// Retry mechanism for file deletion
		int maxRetries = 5;
		int retryCount = 0;
		boolean fileDeleted = false;

		while (retryCount < maxRetries && !fileDeleted) {
			try {
				Files.delete(imagePath);
				fileDeleted = true;
			} catch (NoSuchFileException ex) {
				// Handle case where file to delete is missing
				ex.printStackTrace();
				break; // No need to retry if the file doesn't exist
			} catch (IOException e) {
				// Handle other I/O errors
				e.printStackTrace();
				retryCount++;
				try {
					Thread.sleep(1000); // Wait for 1 second before retrying
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw new RuntimeException("Thread interrupted", ie);
				}
			}
		}

		if (!fileDeleted) {
			throw new RuntimeException("Failed to delete the file after " + maxRetries + " attempts");
		}

		// Delete the category entity
		categoryRepo.delete(foundCategory);
	}

	// Method to retrieve a category by its ID
	@Override
	public CategoryDto getCategory(String categoryId) {
		// Find the category entity by ID or throw an exception if not found
		Category foundCategory = categoryRepo.findById(categoryId).orElseThrow(
				() -> new ResourceNotFoundException("Category with the given id: " + categoryId + " not found!!"));

		// Convert entity to DTO
		return mapper.map(foundCategory, CategoryDto.class);
	}

	// Method to retrieve a list of all categories with pagination and sorting
	@Override
	public List<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// Determine the sort direction
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		// Create a Pageable object with the page number, page size, and sort direction
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		// Retrieve the paginated list of category entities from the repository
		Page<Category> page = categoryRepo.findAll(pageable);
		List<Category> allCategories = page.getContent();

		// Convert the list of category entities to a list of DTOs
		return allCategories.stream().map(category -> mapper.map(category, CategoryDto.class))
				.collect(Collectors.toList());
	}
}
