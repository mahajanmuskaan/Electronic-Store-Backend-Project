package com.springboot.electronicstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.electronicstore.models.Category;

public interface CategoryRepository extends JpaRepository<Category, String>{

}
