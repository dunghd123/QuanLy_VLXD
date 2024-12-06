package com.example.quanly_vlxd.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesRevenueQuaterRequest {
    @NotNull(message = "Year is required")
    private int year;
    @NotNull(message = "Quarter is required")
    @Range(min = 1, max = 4, message = "Quarter must be between 1 and 4")
    private int quarter;
}
