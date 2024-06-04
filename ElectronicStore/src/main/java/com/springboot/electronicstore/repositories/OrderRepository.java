package com.springboot.electronicstore.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.electronicstore.models.Order;
import com.springboot.electronicstore.models.User;

public interface OrderRepository extends JpaRepository<Order, String> {

	List<Order> findByUser(User user);

}
