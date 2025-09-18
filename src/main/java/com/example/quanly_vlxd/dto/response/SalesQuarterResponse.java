package com.example.quanly_vlxd.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesQuarterResponse {
    int quarter;
    BigDecimal total;
}
