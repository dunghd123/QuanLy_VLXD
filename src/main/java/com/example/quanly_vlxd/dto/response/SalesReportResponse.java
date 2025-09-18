package com.example.quanly_vlxd.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesReportResponse {
    private BigDecimal totalRevenue;
    private List<?> SalesDetails;
}
