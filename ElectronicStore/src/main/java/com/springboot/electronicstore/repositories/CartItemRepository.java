package com.springboot.electronicstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.electronicstore.models.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer>{

}
