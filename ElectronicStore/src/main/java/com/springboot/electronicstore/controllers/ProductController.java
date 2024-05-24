package com.springboot.electronicstore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.electronicstore.dtos.ProductDto;
import com.springboot.electronicstore.generalMessage.ApiResponseMessage;
import com.springboot.electronicstore.services.serviceInterfaces.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	// Create Product
	@PostMapping
	public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto product) {

		ProductDto createdProduct = productService.createProduct(product);
		return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
	}

	// Update existing product
	@PutMapping("/{productId}")
	public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto product, @PathVariable String productId) {

		ProductDto updatedProduct = productService.updateProduct(product, productId);
		return new ResponseEntity<>(updatedProduct, HttpStatus.ACCEPTED);
	}

	// Delete existing product
	@DeleteMapping("/{productId}")
	public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId) {

		productService.deleteProduct(productId);
		ApiResponseMessage message = ApiResponseMessage.builder()
				.message("Product is deleted with given Id: " + productId).status(HttpStatus.OK).success(true).build();
		return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
	}

	// Get Single product
	@GetMapping("/{productId}")
	public ResponseEntity<ProductDto> getProduct(@PathVariable String productId) {
		ProductDto foundProduct = productService.getProduct(productId);
		return new ResponseEntity<>(foundProduct, HttpStatus.FOUND);
	}

	// Get List of All products
	@GetMapping
	public ResponseEntity<List<ProductDto>> getAllProducts(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "productTitle", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
		List<ProductDto> allProducts = productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<>(allProducts, HttpStatus.FOUND);
	}

}
