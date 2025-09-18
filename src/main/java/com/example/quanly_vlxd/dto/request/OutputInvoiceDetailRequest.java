package com.example.quanly_vlxd.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class    OutputInvoiceDetailRequest {
    @NotBlank(message = "Product ID is required")
    private int pro_id;

    @NotBlank(message = "Warehouse ID is required")
    private int wh_id;

    @NotBlank(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be a positive number")
    private double quantity;
}
