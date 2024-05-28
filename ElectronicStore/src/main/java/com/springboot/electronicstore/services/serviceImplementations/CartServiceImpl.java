package com.springboot.electronicstore.services.serviceImplementations;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.electronicstore.dtos.AddItemsToCartRequest;
import com.springboot.electronicstore.dtos.CartDto;
import com.springboot.electronicstore.exceptions.customExceptionhandlers.ResourceNotFoundException;
import com.springboot.electronicstore.models.Cart;
import com.springboot.electronicstore.models.CartItem;
import com.springboot.electronicstore.models.Product;
import com.springboot.electronicstore.models.User;
import com.springboot.electronicstore.repositories.CartRepository;
import com.springboot.electronicstore.repositories.ProductRepository;
import com.springboot.electronicstore.repositories.UserRepository;
import com.springboot.electronicstore.services.serviceInterfaces.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CartRepository cartRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public CartDto addItemToCart(String userId, AddItemsToCartRequest request) {
		// TODO Auto-generated method stub

		String productId = request.getProductId();
		int productQuantity = request.getProductQuantity();

		// Fetch the product
		Product foundProduct = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product is not found with given Id: " + productId));

		// Fetch User from dB
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User is not found with given Id: " + userId));

		// Fetch cart if available the get it otherwise create a new one.
		Cart cart = null;
		try {
			cart = cartRepo.findByUser(user).get();

		} catch (NoSuchElementException e) {
			cart = new Cart();
			cart.setCartCreationDate(new Date());
		}

		// Perform Cart Operations
		//List<CartItem> cartItems= cart.getCartItems();
		
		//create Item
		CartItem cartItem =CartItem.builder().cartItemQuantity(productQuantity)
						  .cartItemTotalPrice(productQuantity*foundProduct.getProductOriginalPrice())
						  .carts(cart)
						  .cartItemProduct(foundProduct).build();
		
		cart.getCartItems().add(cartItem);

		return null;
	}

	@Override
	public void removeItemFromCart(String userId, int cartItem) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearCart(String userId) {
		// TODO Auto-generated method stub

	}

}
