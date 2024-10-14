package com.example.quanly_vlxd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    @NotBlank(message = "Customer ID is required")
    @Size(max = 20, message = "Customer ID must not exceed 20 characters")
    private String id;

    @NotBlank(message = "Customer name is required")
    private String name;

    @NotBlank(message = "Customer address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be valid and contain only digits")
    private String phoneNum;

    private boolean isActive;
}
