package com.springboot.electronicstore.dtos;

import com.springboot.electronicstore.models.Category;

import jakarta.validation.constraints.NotBlank;
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
public class CategoryDto {

    // Unique identifier for the category
    private String categoryId;
    
    // Title of the category, must not be blank
    @NotBlank
    private String categoryTitle;
    
    // Description of the category, must not be blank
    @NotBlank
    private String categoryDescription;
    
    // Image associated with the category
    private String categoryImage;
}
