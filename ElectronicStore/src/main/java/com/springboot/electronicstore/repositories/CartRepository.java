package com.springboot.electronicstore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.electronicstore.models.Cart;
import com.springboot.electronicstore.models.User;

public interface CartRepository extends JpaRepository<Cart, String>{
	
	Optional<Cart> findByUser(User user);

}
