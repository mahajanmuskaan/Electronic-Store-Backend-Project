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
import org.springframework.web.bind.annotation.RestController;

import com.springboot.electronicstore.dtos.CreateOrderRequest;
import com.springboot.electronicstore.dtos.OrderDto;
import com.springboot.electronicstore.generalMessage.ApiResponseMessage;
import com.springboot.electronicstore.services.serviceInterfaces.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	// Create the order
	@PostMapping
	public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request) {
		OrderDto order = orderService.createOrder(request);
		return new ResponseEntity<>(order, HttpStatus.CREATED);
	}

	// Delete the order
	@DeleteMapping("/{orderId}")
	public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId) {
		orderService.removeOrder(orderId);
		ApiResponseMessage responseMessage = ApiResponseMessage.builder().status(HttpStatus.OK)
				.message("order is removed !!").success(true).build();
		return new ResponseEntity<>(responseMessage, HttpStatus.OK);

	}

	// Get orders of the user
	@GetMapping("/users/{userId}")
	public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId) {
		List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);
		return new ResponseEntity<>(ordersOfUser, HttpStatus.OK);
	}

	// Update order
	@PutMapping("/{orderId}")
	public ResponseEntity<OrderDto> updateOrder(@PathVariable String orderId, @RequestBody CreateOrderRequest request) {
		return new ResponseEntity<>(orderService.updateOrder(orderId, request), HttpStatus.OK);
	}

}
