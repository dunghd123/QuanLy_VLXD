package com.example.quanly_vlxd.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InputInvoiceDetailRequest {

    private int id;

    @NotBlank(message = "Product ID is required")
    private int proId;

    @NotBlank(message = "Warehouse ID is required")
    private int whId;

    @NotBlank(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be a positive number")
    private double quantity;

}
