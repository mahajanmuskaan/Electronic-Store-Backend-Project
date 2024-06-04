package com.springboot.electronicstore.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.springboot.electronicstore.dtos.ProductDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class Cart {
	
	@Id
	@Column(name="cart_id")
	private String cartId;
	
	@Column(name="cart_creationdate")
	private Date cartCreationDate;
	
	@OneToOne
	@JoinColumn(name="cart_userId")
	private User cartUser;
	
	// Mapping cart-Items
	@OneToMany(mappedBy="carts",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
	private List<CartItem> cartItems = new ArrayList<>();
	

}
