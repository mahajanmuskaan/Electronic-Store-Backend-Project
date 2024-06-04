package com.springboot.electronicstore.services.serviceImplementations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.electronicstore.dtos.AddItemsToCartRequest;
import com.springboot.electronicstore.dtos.CartDto;
import com.springboot.electronicstore.exceptions.customExceptionhandlers.BadApiRequest;
import com.springboot.electronicstore.exceptions.customExceptionhandlers.ResourceNotFoundException;
import com.springboot.electronicstore.models.Cart;
import com.springboot.electronicstore.models.CartItem;
import com.springboot.electronicstore.models.Product;
import com.springboot.electronicstore.models.User;
import com.springboot.electronicstore.repositories.CartItemRepository;
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
	private CartItemRepository cartItemRepo;

	@Autowired
	private ModelMapper mapper;

	Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

	@Override
	public CartDto addItemToCart(String userId, AddItemsToCartRequest request) {
		String productId = request.getProductId();
		int productQuantity = request.getProductQuantity();

		logger.info("Product Id is : {} " + productId);

		// Fetch the product from the database by its ID
		Product foundProduct = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product is not found with given Id: " + productId));

		logger.info("Found Product is : {} " + foundProduct);

		// Fetch the user from the database by their ID
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User is not found with given Id: " + userId));

		// Check if the product quantity is valid
		if (productQuantity <= 0) {
			throw new BadApiRequest("Requested Quantity is not supported!!");
		}

		// Fetch the cart for the user. If it does not exist, create a new cart.
		Cart cart = cartRepo.findByCartUser(user).orElseGet(() -> {
			Cart newCart = new Cart();
			newCart.setCartId(UUID.randomUUID().toString());
			newCart.setCartCreationDate(new Date());
			newCart.setCartUser(user);
			return newCart;
		});

		// Flag to check if the cart needs updating
		AtomicReference<Boolean> updated = new AtomicReference<>(false);

		// Get the current items in the cart
		List<CartItem> cartItems = cart.getCartItems();
		logger.info("cartItems are :" + cartItems.isEmpty());
		if (cartItems.isEmpty()) {
			cartItems = new ArrayList<>();
		}

		// Iterate through the cart items to update the quantity if the product is
		// already in the cart
		for (CartItem item : cartItems) {
			if (item.getCartItemProduct().getProductId().equals(productId)) {
				// If the product is already in the cart, update the quantity and total price
				item.setCartItemQuantity(item.getCartItemQuantity() + productQuantity);
				item.setCartItemTotalPrice(
						item.getCartItemQuantity() * item.getCartItemProduct().getProductDiscountedPrice());
				updated.set(true);
				break; // Break the loop since we found the product
			}
		}

		if (!updated.get()) {
			// If the product is not in the cart, create a new cart item and add it to the
			// cart
			CartItem cartItem = CartItem.builder().cartItemQuantity(productQuantity)
					.cartItemTotalPrice(productQuantity * foundProduct.getProductDiscountedPrice())
					.cartItemProduct(foundProduct).carts(cart).build();

			cartItems.add(cartItem);
		}

		// Set the updated items to the cart
		cart.setCartItems(cartItems);

		// Save the updated cart to the database
		Cart updatedCart = cartRepo.save(cart);

		// Convert the cart entity to a DTO and return it
		return mapper.map(updatedCart, CartDto.class);
	}

	@Override
	public void removeItemFromCart(String userId, int cartItemId) {
		// Fetch the cart item by its ID. If not found, throw an exception.
		CartItem foundCartItem = cartItemRepo.findById(cartItemId)
				.orElseThrow(() -> new ResourceNotFoundException("CartItem is not found with given Id: " + cartItemId));

		// Delete the cart item from the database
		cartItemRepo.delete(foundCartItem);
	}

	@Override
	public void clearCart(String userId) {
		// Fetch the user from the database by their ID
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User is not found with given Id: " + userId));

		// Fetch the cart for the user. If not found, throw an exception.
		Cart foundCart = cartRepo.findByCartUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("Cart is not found !!!"));

		// Clear all items from the cart
		foundCart.getCartItems().clear();

		// Save the updated cart to the database
		cartRepo.save(foundCart);
	}

	public CartDto getCartByUser(String userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));
		Cart cart = cartRepo.findByCartUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found !!"));
		return mapper.map(cart, CartDto.class);
	}
}
