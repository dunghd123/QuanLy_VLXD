package com.example.quanly_vlxd.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InputInvoiceDetailDTO {
    @Min(value = 1, message = "Product ID must be a positive number")
    private int PRO_ID;

    @Min(value = 1, message = "Invoice ID must be a positive number")
    private int INP_ID;

    @Min(value = 1, message = "Quantity must be a positive number")
    private int QUANTITY;

    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than zero")
    private BigDecimal UNIT_PRICE;
}
