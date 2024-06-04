package com.springboot.electronicstore.services.serviceInterfaces;

import java.util.List;

import com.springboot.electronicstore.dtos.CategoryDto;

public interface CategoryService {
	
	// create Category
	CategoryDto createCategory(CategoryDto category);
	
	// update Category
	CategoryDto updateCategory(CategoryDto category, String categoryId);
	
	// delete
	void deleteCategory(String categoryId);
	
	// get single category
	CategoryDto getCategory(String categoryId);
	
	// get all categories
	List<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir);
	
}
