package com.example.quanly_vlxd.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesProductResponse {
    private Integer proId;
    private String proName;
    private Double quantity;
    private Double totalAmount;
}
