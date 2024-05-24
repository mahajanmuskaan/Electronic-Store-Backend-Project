package com.springboot.electronicstore.services.serviceImplementations;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.springboot.electronicstore.dtos.ProductDto;
import com.springboot.electronicstore.exceptions.customExceptionhandlers.ResourceNotFoundException;
import com.springboot.electronicstore.models.Product;
import com.springboot.electronicstore.repositories.ProductRepository;
import com.springboot.electronicstore.services.serviceInterfaces.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ProductRepository productRepo;

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

}
