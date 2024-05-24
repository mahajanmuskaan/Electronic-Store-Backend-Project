package com.springboot.electronicstore.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

@Entity
@Table(name="product")
public class Product {
	
	@Id
	@Column(name="product_id")
	private String productId;
	
	@NotBlank
	@Column(name="product_title")
	private String productTitle;
	
	@NotBlank
	@Column(name="product_description", length=1000)
	private String productDescription;
	
	@Column(name="product_quantity")
	private String productQuantity;
	
	@Column(name="product_origprice")
	private int productOriginalPrice;
	
	@Column(name="product_discountprice")
	private int productDiscountedPrice;
	
	@Column(name="product_dateadded")
	private Date productAdded;
	
	@Column(name="product_islive")
	private boolean productIsLive;
	
	@Column(name="product_instock")
	private boolean productInStock;
	
}
