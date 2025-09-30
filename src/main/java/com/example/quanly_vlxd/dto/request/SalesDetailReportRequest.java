package com.example.quanly_vlxd.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesDetailReportRequest {
    private String startDate;
    private String endDate;
    private String typeReport;
}