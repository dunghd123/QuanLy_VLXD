package com.example.quanly_vlxd.dto.request;

import com.example.quanly_vlxd.enums.PriceTypeEnums;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceFilterRequest {
    private String invoiceTypeFilter;
    private String productNameFilter;
    private String startDateFilter;
    private String endDateFilter;
    private int pageFilter;
    private int sizeFilter;
    private PriceTypeEnums priceTypeFilter;
}
