package com.example.quanly_vlxd.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank(message = "Product name cannot be empty")
    @Size(max = 100, message = "Product name should not exceed 100 characters")
    private String name;

    @NotBlank(message = "Unit of measure cannot be empty")
    @Size(max = 50, message = "Unit of measure should not exceed 50 characters")
    private String unitMeasure;

    @NotNull(message = "Category ID cannot be null")
    private Integer categoryId;

    @NotNull(message = "IsActive status cannot be null")
    private Boolean isActive;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}
