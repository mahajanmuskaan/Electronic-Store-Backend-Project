package com.springboot.electronicstore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.electronicstore.models.Cart;
import com.springboot.electronicstore.models.User;

/**
 * Repository interface for Cart entity.
 * Extends JpaRepository to provide CRUD operations.
 */
public interface CartRepository extends JpaRepository<Cart, String> {

    /**
     * Custom method to find a Cart by its associated User.
     * 
     * @param user the User entity
     * @return an Optional containing the found Cart or empty if no cart found
     */
    Optional<Cart> findByCartUser(User user);
}

