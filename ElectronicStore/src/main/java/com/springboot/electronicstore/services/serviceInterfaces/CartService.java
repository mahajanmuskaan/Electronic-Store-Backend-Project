package com.springboot.electronicstore.services.serviceInterfaces;

import com.springboot.electronicstore.dtos.AddItemsToCartRequest;
import com.springboot.electronicstore.dtos.CartDto;

public interface CartService {

	// Add Items to Cart
	/*
	 * case1: If cart for user is not available, then we will create cart and then
	 * add items in it. 
	 * case2: If cart for user is available, then we will add items
	 * to cart. 
	 * case3: If product is already present in cart, then we will increase
	 * the quantity of that product
	 */
	CartDto addItemToCart(String userId, AddItemsToCartRequest request);

	// Remove Items from Cart
	void removeItemFromCart(String userId, int cartItem);

	// Clear Cart
	void clearCart(String userId);

}
