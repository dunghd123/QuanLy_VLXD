package com.example.quanly_vlxd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductDTO {

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name should not exceed 100 characters")
    private String name;

    @NotBlank(message = "Unit measure cannot be blank")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Unit measure should only contain letters")
    @Size(max = 20, message = "Unit measure should not exceed 20 characters")
    private String unitMeasure;

    @Size(max = 255, message = "Description should not exceed 255 characters")
    private String description;

    @NotBlank(message = "Category name cannot be blank")
    @Size(max = 50, message = "Category name should not exceed 50 characters")
    private String cateName;
}