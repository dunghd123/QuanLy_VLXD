package com.example.quanly_vlxd.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesMonthResponse {
    private int month;
    private double totalAmount;
}
