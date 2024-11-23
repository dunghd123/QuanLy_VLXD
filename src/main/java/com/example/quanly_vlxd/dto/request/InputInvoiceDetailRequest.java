package com.example.quanly_vlxd.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InputInvoiceDetailRequest {

    @NotBlank(message = "Product ID is required")
    @Min(value = 1, message = "Product ID must be a positive number")
    private int PRO_ID;

    @NotBlank(message = "Warehouse ID is required")
    @Min(value = 1, message = "Warehouse ID must be a positive number")
    private int WH_ID;

    @NotBlank(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be a positive number")
    private int Quantity;

}
