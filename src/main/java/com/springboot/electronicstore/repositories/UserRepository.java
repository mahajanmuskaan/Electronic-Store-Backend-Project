package com.springboot.electronicstore.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.electronicstore.models.User;

// UserRepository interface to perform CRUD operations and custom queries on User entities
public interface UserRepository extends JpaRepository<User, String> {

	// Method to find a user by their email address
	Optional<User> findByUserEmail(String userEmail);

	// Method to find users whose names contain a specified keyword
	List<User> findByUserNameContaining(String keyword);
}
