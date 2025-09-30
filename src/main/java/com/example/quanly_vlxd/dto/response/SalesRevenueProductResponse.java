package com.example.quanly_vlxd.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesRevenueProductResponse {
    private Integer proId;
    private String proName;
    private Integer totalQuantity;
    private Double totalAmount;
}
