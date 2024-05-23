package com.springboot.electronicstore.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Lombok annotations to generate getters, setters, no-args constructor, all-args constructor, and builder pattern methods
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

// JPA annotation to specify this class is an entity and is mapped to a database table
@Entity
// JPA annotation to define the table name in the database for this entity
@Table(name = "category")
public class Category {
    
    // Primary key field with JPA annotation to specify the column name in the database
    @Id
    @Column(name = "category_id")
    private String categoryId;
    
    // JPA annotation to specify the column name in the database
    @Column(name = "category_title")
    private String categoryTitle;
    
    // JPA annotation to specify the column name in the database
    @Column(name = "category_description")
    private String categoryDescription;
    
    // JPA annotation to specify the column name in the database
    @Column(name = "category_image")
    private String categoryImage;
}
