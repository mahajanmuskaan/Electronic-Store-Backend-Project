package com.springboot.electronicstore.services.serviceImplementations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
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
import com.springboot.electronicstore.dtos.ProductDto;
import com.springboot.electronicstore.exceptions.customExceptionhandlers.ResourceNotFoundException;
import com.springboot.electronicstore.models.Category;
import com.springboot.electronicstore.models.Product;
import com.springboot.electronicstore.repositories.CategoryRepository;
import com.springboot.electronicstore.repositories.ProductRepository;
import com.springboot.electronicstore.services.serviceInterfaces.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private CategoryRepository categoryRepo;

	@Value("${product.profile.image.path}")
	private String relativePath;

	@Override
	public ProductDto createProduct(ProductDto product) {
		// TODO Auto-generated method stub
		// Dto -> Entity

		String productId = UUID.randomUUID().toString();
		product.setProductId(productId);

		product.setProductAdded(new Date());

		Product productEntity = mapper.map(product, Product.class);
		Product savedProductEntity = productRepo.save(productEntity);

		// Entity -> Dto
		ProductDto newProductDto = mapper.map(savedProductEntity, ProductDto.class);
		return newProductDto;
	}

	@Override
	public ProductDto updateProduct(ProductDto product, String productId) {
		// TODO Auto-generated method stub
		Product foundProduct = productRepo.findById(productId).orElseThrow(
				() -> new ResourceNotFoundException("Product is not found with given id : " + productId + " !!"));

		// Update the existing values of entity
		foundProduct.setProductId(product.getProductId());
		foundProduct.setProductTitle(product.getProductTitle());
		foundProduct.setProductDescription(product.getProductDescription());
		foundProduct.setProductAdded(product.getProductAdded());
		foundProduct.setProductDiscountedPrice(product.getProductDiscountedPrice());
		foundProduct.setProductOriginalPrice(product.getProductOriginalPrice());
		foundProduct.setProductQuantity(product.getProductQuantity());
		foundProduct.setProductIsLive(product.isProductIsLive());
		foundProduct.setProductInStock(product.isProductInStock());
		foundProduct.setProductImage(product.getProductImage());

		Product updatedProduct = productRepo.save(foundProduct);

		// Entity -> DTO
		ProductDto updatedProductDto = mapper.map(updatedProduct, ProductDto.class);

		return updatedProductDto;
	}

	@Override
	public void deleteProduct(String productId) {
		// TODO Auto-generated method stub
		// Delete the category entity
		Product foundProduct = productRepo.findById(productId).orElseThrow(
				() -> new ResourceNotFoundException("Product is not found with given id : " + productId + " !!"));

		// Get full path of image folder and concatenate with image filename
		Path directoryPath = Paths.get(System.getProperty("user.dir"), relativePath);
		String fullpath = directoryPath + File.separator + foundProduct.getProductImage();
		Path imagePath = Paths.get(fullpath);

		// Delete product image file
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

		productRepo.delete(foundProduct);
	}

	@Override
	public ProductDto getProduct(String productId) {
		// TODO Auto-generated method stub
		Product foundProduct = productRepo.findById(productId).orElseThrow(
				() -> new ResourceNotFoundException("Product is not found with given id : " + productId + " !!"));

		ProductDto foundProductDto = mapper.map(foundProduct, ProductDto.class);

		return foundProductDto;
	}

	@Override
	public List<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		// Determine the sort direction
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		// Create a Pageable object with the page number, page size, and sort direction
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		// Retrieve the paginated list of category entities from the repository
		Page<Product> page = productRepo.findAll(pageable);
		List<Product> allproducts = page.getContent();

		// Convert the list of category entities to a list of DTOs
		return allproducts.stream().map(product -> mapper.map(product, ProductDto.class)).collect(Collectors.toList());
	}

	// Create Product with category
	public ProductDto createProductWithCategory(ProductDto product, String categoryId) {

		Category foundCategory = categoryRepo.findById(categoryId).orElseThrow(
				() -> new ResourceNotFoundException("Category is not found with given id : " + categoryId + " !!"));

		String productId = UUID.randomUUID().toString();
		product.setProductId(productId);

		product.setProductAdded(new Date());

		Product productEntity = mapper.map(product, Product.class);
		productEntity.setCategorys(foundCategory);

		Product savedProductEntity = productRepo.save(productEntity);

		// Entity -> Dto
		ProductDto newProductDto = mapper.map(savedProductEntity, ProductDto.class);
		return newProductDto;

	}

	// Update Category in product
	@Override
	public ProductDto updateCategoryProduct(String productId, String categoryId) {
		// TODO Auto-generated method stub
		Product foundProduct = productRepo.findById(productId).orElseThrow(
				() -> new ResourceNotFoundException("Product is not found with given id : " + productId + " !!"));

		Category foundCategory = categoryRepo.findById(categoryId).orElseThrow(
				() -> new ResourceNotFoundException("Category is not found with given id : " + categoryId + " !!"));

		foundProduct.setCategorys(foundCategory);
		Product savedProductEntity = productRepo.save(foundProduct);

		// Entity -> Dto
		ProductDto newProductDto = mapper.map(savedProductEntity, ProductDto.class);

		return newProductDto;
	}

}
