package com.springboot.electronicstore.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name="order_table")
public class Order {
	
	@Id
	private String orderId;
	
	// Order Status - PENDING / DISPATCHED / DELIVERED
	private String orderStatus;
	
	// Payment Status - NOT-PAID / PAID
	private String paymentStatus;
	
	private int orderAmount;
	
	@Column(length=1000)
	private String billingAddress;

	private String billingPhone;
	
	private Date orderedDate;

	private Date deliveredDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User user;
	
	@OneToMany(mappedBy = "orders",fetch = FetchType.EAGER, cascade = CascadeType.ALL,orphanRemoval = true)
	private List<OrderItem> orderItems = new ArrayList<>();

}
