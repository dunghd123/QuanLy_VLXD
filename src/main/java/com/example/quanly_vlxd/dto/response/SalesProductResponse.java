package com.example.quanly_vlxd.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesProductResponse {
    private int proId;
    private String proName;
    private double quantity;
    private BigDecimal total;
}
