package com.springboot.electronicstore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Lombok annotations to generate getters, setters, no-args constructor, all-args constructor, and builder pattern methods
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class OrderItemDto {
	private int orderItemId;

	private int quantity;

	private int totalPrice;

	private ProductDto products;

	//private OrderDto orders;

}
