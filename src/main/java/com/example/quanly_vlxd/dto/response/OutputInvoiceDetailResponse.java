package com.example.quanly_vlxd.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutputInvoiceDetailResponse {
    private int id;
    private String productName;
    private String unitMeasure;
    private double quantity;
    private double price;
    private double amount;
    private String warehouseName;
}
