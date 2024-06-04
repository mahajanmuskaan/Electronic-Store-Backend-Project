package com.springboot.electronicstore.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import com.springboot.electronicstore.dtos.AddItemsToCartRequest;
import com.springboot.electronicstore.dtos.CartDto;
import com.springboot.electronicstore.generalMessage.ApiResponseMessage;
import com.springboot.electronicstore.services.serviceInterfaces.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	// Add Items to Cart
	@PutMapping("/{userId}")
	public ResponseEntity<CartDto> addItemsToCart(@RequestBody AddItemsToCartRequest request,
			@PathVariable String userId) {

		CartDto cartDto = cartService.addItemToCart(userId, request);

		return new ResponseEntity<>(cartDto, HttpStatus.OK);
	}

	// Remove Items from Cart
	@DeleteMapping("/{userId}/items/{itemId}")
	public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId,
			@PathVariable int itemId) {
		cartService.removeItemFromCart(userId, itemId);
		ApiResponseMessage response = ApiResponseMessage.builder().message("Item is removed !!").success(true)
				.status(HttpStatus.OK).build();
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	// clear cart
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId) {
		cartService.clearCart(userId);
		ApiResponseMessage response = ApiResponseMessage.builder().message("Now cart is blank !!").success(true)
				.status(HttpStatus.OK).build();
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	// Get cart by user
	@GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable String userId) {
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

}
