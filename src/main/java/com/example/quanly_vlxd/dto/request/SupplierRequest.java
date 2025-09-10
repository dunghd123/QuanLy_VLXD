package com.example.quanly_vlxd.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequest {

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 100, message = "Name should not exceed 100 characters")
    private String name;

    @NotBlank(message = "Address cannot be empty")
    @Size(max = 255, message = "Address should not exceed 255 characters")
    private String address;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number should be between 10 and 15 digits")
    private String phone;
}
