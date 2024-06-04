package com.springboot.electronicstore.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.electronicstore.dtos.CategoryDto;
import com.springboot.electronicstore.dtos.ProductDto;
import com.springboot.electronicstore.generalMessage.ApiResponseMessage;
import com.springboot.electronicstore.generalMessage.ImageResponseMessage;
import com.springboot.electronicstore.repositories.CategoryRepository;
import com.springboot.electronicstore.services.serviceInterfaces.FileService;
import com.springboot.electronicstore.services.serviceInterfaces.ProductService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private FileService fileService;

	@Value("${product.profile.image.path}")
	private String fileUploadPath;

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

	@PostMapping("/image/{productId}")
	public ResponseEntity<ImageResponseMessage> uploadUserImage(@PathVariable String productId,
			@RequestParam("productImage") MultipartFile productImage) throws IOException {

		// Upload the product image file
		String productImageName = fileService.uploadFile(productImage, fileUploadPath);

		// Update the product's image name in the database
		ProductDto product = productService.getProduct(productId);
		product.setProductImage(productImageName);
		productService.updateProduct(product, productId);

		// Build the image upload response message
		ImageResponseMessage imageResponse = ImageResponseMessage.builder().userImage(productImageName)
				.message("Image Name is updated!").success(true).status(HttpStatus.OK).build();

		return new ResponseEntity<>(imageResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/image/{productId}", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
	public void serveUserImageFile(@PathVariable String productId, HttpServletResponse response) throws IOException {

		// Retrieve the category's image file
		ProductDto product = productService.getProduct(productId);
		InputStream resource = fileService.getResource(fileUploadPath, product.getProductImage());

		// Set the response content type as image/jpeg or image/png
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		response.setHeader("Content-Disposition", "inline; filename=" + product.getProductImage());

		// Copy the image input stream to the response output stream
		StreamUtils.copy(resource, response.getOutputStream());
	}

}
