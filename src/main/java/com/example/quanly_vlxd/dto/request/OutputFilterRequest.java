package com.example.quanly_vlxd.dto.request;

import com.example.quanly_vlxd.enums.InvoiceStatusEnums;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutputFilterRequest {
    private String cusNameFilter;
    private InvoiceStatusEnums statusFilter;
    private int pageFilter;
    private int sizeFilter;
}
