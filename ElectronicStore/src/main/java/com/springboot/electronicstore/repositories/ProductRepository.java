package com.springboot.electronicstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.electronicstore.models.Product;

public interface ProductRepository extends JpaRepository<Product, String>{

}
