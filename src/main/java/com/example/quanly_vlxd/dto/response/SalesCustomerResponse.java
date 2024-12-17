package com.example.quanly_vlxd.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesCustomerResponse {
    private int cusId;
    private String cusName;
    private int outputInvoiceId;
    private String total;
}
