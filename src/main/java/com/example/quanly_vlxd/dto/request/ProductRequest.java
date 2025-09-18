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
    private String name;

    @NotBlank(message = "Unit of measure cannot be empty")
    @Size(max = 10, message = "Unit of measure should not exceed 10 characters")
    private String unitMeasure;

    @NotNull(message = "Category ID cannot be null")
    private Integer cateId;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}
