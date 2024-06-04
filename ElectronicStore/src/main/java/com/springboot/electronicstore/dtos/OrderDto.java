package com.springboot.electronicstore.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

public class OrderDto {

	private String orderId;

	private String orderStatus="PENDING";

	private String paymentStatus="NOT-PAID";

	private int orderAmount;

	private String billingAddress;

	private String billingPhone;

	private Date orderedDate = new Date();

	private Date deliveredDate;

	private UserDto user;

	private List<OrderItemDto> orderItems = new ArrayList<>();

}
