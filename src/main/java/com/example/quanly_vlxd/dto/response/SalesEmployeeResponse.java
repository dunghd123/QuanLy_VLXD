package com.example.quanly_vlxd.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesEmployeeResponse {
    private Integer empId;
    private String empName;
    private Double totalAmount;
}
