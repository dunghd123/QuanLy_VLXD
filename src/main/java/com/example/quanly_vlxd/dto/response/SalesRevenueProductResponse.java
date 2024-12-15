package com.example.quanly_vlxd.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesRevenueProductResponse {
    private int proId;
    private String proName;
    private double inputTotal;
    private double outputTotal;
    private double revenue;
}
