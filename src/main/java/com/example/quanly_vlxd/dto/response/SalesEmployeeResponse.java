package com.example.quanly_vlxd.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesEmployeeResponse {
    private int empId;
    private String empName;
    private int outputInvoiceId;
    private String total;
}
