package com.springboot.electronicstore.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
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
public class ProductDto {

	private String productId;

	@NotBlank
	private String productTitle;

	@NotBlank
	private String productDescription;

	private String productQuantity;

	private int productOriginalPrice;

	private int productDiscountedPrice;

	private Date productAdded;

	private boolean productIsLive;

	private boolean productInStock;

}
