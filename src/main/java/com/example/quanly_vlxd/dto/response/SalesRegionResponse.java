package com.example.quanly_vlxd.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesRegionResponse {
    private String region;
    private BigDecimal totalRevenue;
}
