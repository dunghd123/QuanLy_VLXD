package com.example.quanly_vlxd.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierDetailResonse {
    private int suppId;
    private String suppName;
    private int inputInvoiceId;
    private String total;
}
