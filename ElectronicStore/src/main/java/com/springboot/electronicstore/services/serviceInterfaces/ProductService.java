package com.springboot.electronicstore.services.serviceInterfaces;

import java.util.List;
import com.springboot.electronicstore.dtos.ProductDto;

public interface ProductService {
	
	// Create Product
	ProductDto createProduct(ProductDto product);
	
	// Update existing Product
	ProductDto updateProduct(ProductDto product, String productId);
	
	// Delete existing product
	void deleteProduct(String productId);
	
	// Get single product
	ProductDto getProduct(String productId);
	
	// Get List of all products
	List<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir);
}
