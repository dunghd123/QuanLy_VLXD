package com.example.quanly_vlxd.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesProductResponse {
    private int proId;
    private String proName;
    private double quantity;
    private double total;
}
