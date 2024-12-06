
package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.request.SalesDetailReportRequest;
import com.example.quanly_vlxd.dto.request.SalesRevenueQuaterRequest;
import com.example.quanly_vlxd.dto.response.SalesDetailResponse;
import com.example.quanly_vlxd.dto.response.SalesQuaterResponse;
import com.example.quanly_vlxd.dto.response.SalesReportResponse;

import java.util.List;

public interface SalesReportService {

    List<SalesDetailResponse> generateSalesReportDetailed(SalesDetailReportRequest request);
    SalesReportResponse generateSalesReportRevenue(SalesDetailReportRequest request);
    SalesReportResponse generateSalesReportByQuater(SalesRevenueQuaterRequest request);
    List<SalesQuaterResponse> allQuarterReport(int year);

}