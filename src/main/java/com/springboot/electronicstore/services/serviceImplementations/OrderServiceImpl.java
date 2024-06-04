package com.springboot.electronicstore.services.serviceImplementations;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.electronicstore.dtos.CreateOrderRequest;
import com.springboot.electronicstore.dtos.OrderDto;
import com.springboot.electronicstore.exceptions.customExceptionhandlers.BadApiRequest;
import com.springboot.electronicstore.exceptions.customExceptionhandlers.ResourceNotFoundException;
import com.springboot.electronicstore.models.Cart;
import com.springboot.electronicstore.models.CartItem;
import com.springboot.electronicstore.models.Order;
import com.springboot.electronicstore.models.OrderItem;
import com.springboot.electronicstore.models.User;
import com.springboot.electronicstore.repositories.CartRepository;
import com.springboot.electronicstore.repositories.OrderRepository;
import com.springboot.electronicstore.repositories.UserRepository;
import com.springboot.electronicstore.services.serviceInterfaces.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CartRepository cartRepository;

	@Override
	public OrderDto createOrder(CreateOrderRequest orderDto) {
		// Extract user ID and cart ID from the request
		String userId = orderDto.getUserId();
		String cartId = orderDto.getCartId();

		// Fetch the user from the database
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given id !!"));

		// Fetch the cart from the database
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart with given id not found on server !!"));

		// Get the list of items in the cart
		List<CartItem> cartItems = cart.getCartItems();

		// Check if the cart is empty
		if (cartItems.size() <= 0) {
			throw new BadApiRequest("Invalid number of items in cart !!");
		}

		// Create a new order object
		Order order = Order.builder().billingPhone(orderDto.getBillingPhone())
				.billingAddress(orderDto.getBillingAddress()).orderedDate(new Date()).deliveredDate(null)
				.paymentStatus(orderDto.getPaymentStatus()).orderStatus(orderDto.getOrderStatus())
				.orderId(UUID.randomUUID().toString()).user(user).build();

		// Calculate the total order amount and convert cart items to order items
		AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
		List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
			// Convert CartItem to OrderItem
			OrderItem orderItem = OrderItem.builder().quantity(cartItem.getCartItemQuantity())
					.products(cartItem.getCartItemProduct())
					.totalPrice(
							cartItem.getCartItemQuantity() * cartItem.getCartItemProduct().getProductDiscountedPrice())
					.orders(order).build();

			// Add the item price to the total order amount
			orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
			return orderItem;
		}).collect(Collectors.toList());

		// Set the order items and total amount in the order
		order.setOrderItems(orderItems);
		order.setOrderAmount(orderAmount.get());

		// Clear the cart items after creating the order
		cart.getCartItems().clear();
		cartRepository.save(cart);

		// Save the order to the database
		Order savedOrder = orderRepository.save(order);

		// Convert the saved order to a DTO and return it
		return modelMapper.map(savedOrder, OrderDto.class);
	}

	@Override
	public void removeOrder(String orderId) {
		// Fetch the order from the database
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("order is not found !!"));

		// Delete the order from the database
		orderRepository.delete(order);
	}

	@Override
	public List<OrderDto> getOrdersOfUser(String userId) {
		// Fetch the user from the database
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found !!"));

		// Fetch the orders of the user from the database
		List<Order> orders = orderRepository.findByUser(user);

		// Convert the list of orders to a list of DTOs and return it
		List<OrderDto> orderDtos = orders.stream().map(order -> modelMapper.map(order, OrderDto.class))
				.collect(Collectors.toList());

		return orderDtos;
	}

	@Override
	public OrderDto updateOrder(String orderId, CreateOrderRequest request) {
		// TODO Auto-generated method stub
		
		String orderStatus = request.getOrderStatus();
		String paymentStatus = request.getPaymentStatus();
		
		Order foundOrder = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found with given id !!"));
		foundOrder.setOrderStatus(orderStatus);
		foundOrder.setPaymentStatus(paymentStatus);
		foundOrder.setDeliveredDate(new Date());
		
		Order updatedOrder = orderRepository.save(foundOrder);
		
		return modelMapper.map(updatedOrder, OrderDto.class) ;
	}

}
