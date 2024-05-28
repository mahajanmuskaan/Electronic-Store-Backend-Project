package com.springboot.electronicstore.dtos;

import java.util.Date;
import java.util.List;

import com.springboot.electronicstore.models.Cart;

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

public class CartItemDto {

	private int cartItemId;

	private ProductDto cartItemProduct;

	private int cartItemQuantity;

	private int cartItemTotalPrice;

}
