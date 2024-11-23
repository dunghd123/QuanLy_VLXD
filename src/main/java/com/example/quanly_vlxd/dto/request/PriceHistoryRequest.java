package com.example.quanly_vlxd.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceHistoryRequest {
    @NotNull(message = "Product ID cannot be null")
    private Integer productId;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must have two decimal places")
    private double price;

    @NotNull(message = "Invoice type cannot be null")
    @Pattern(regexp = "^(INPUT|OUTPUT)$", message = "Invoice type must be either 'INPUT' or 'OUTPUT'")
    private String invoiceType;

}
