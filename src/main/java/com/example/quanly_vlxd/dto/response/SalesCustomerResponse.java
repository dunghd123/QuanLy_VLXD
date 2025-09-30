package com.example.quanly_vlxd.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesCustomerResponse {
    private Integer cusId;
    private String cusName;
    private Double totalAmount;
}
