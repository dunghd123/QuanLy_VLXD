package com.example.quanly_vlxd.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class SaleReportResponse {
    private Summary summary;
    private List<?> details;
    @Data
    @Builder
    public static class Summary {
        private BigDecimal totalRevenue;
        private Integer recordCount;
    }
}
