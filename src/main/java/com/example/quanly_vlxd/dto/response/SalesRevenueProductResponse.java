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
    private String inputTotal;
    private String outputTotal;
    private String revenue;
}
