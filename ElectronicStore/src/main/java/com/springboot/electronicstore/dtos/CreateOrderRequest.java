package com.springboot.electronicstore.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CreateOrderRequest {
	private String cartId;

	@NotBlank(message = "Cart id is required !!")
	private String userId;

	private String orderStatus = "PENDING";
	private String paymentStatus = "NOT-PAID";
	@NotBlank(message = "Address is required !!")
	private String billingAddress;
	@NotBlank(message = "Phone number is required !!")
	private String billingPhone;

}
