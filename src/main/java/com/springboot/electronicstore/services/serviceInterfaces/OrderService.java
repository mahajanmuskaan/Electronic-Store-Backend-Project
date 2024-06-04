package com.springboot.electronicstore.services.serviceInterfaces;

import java.util.List;

import com.springboot.electronicstore.dtos.CreateOrderRequest;
import com.springboot.electronicstore.dtos.OrderDto;

public interface OrderService {

	// create order
	OrderDto createOrder(CreateOrderRequest orderDto);

	// remove order
	void removeOrder(String orderId);

	// get orders of user
	List<OrderDto> getOrdersOfUser(String userId);
	
	// update Order
	OrderDto updateOrder(String orderId, CreateOrderRequest orderDto);

}
