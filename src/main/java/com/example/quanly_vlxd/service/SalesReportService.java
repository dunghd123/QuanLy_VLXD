
package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.request.SalesDetailReportRequest;
import com.example.quanly_vlxd.dto.request.SalesRevenueByRegionRequest;
import com.example.quanly_vlxd.dto.request.SalesRevenueQuarterRequest;
import com.example.quanly_vlxd.dto.response.SalesDetailResponse;
import com.example.quanly_vlxd.dto.response.SalesQuarterResponse;
import com.example.quanly_vlxd.dto.response.SalesReportResponse;

import java.util.List;

public interface SalesReportService {

    List<SalesDetailResponse> generateSalesReportDetailed(SalesDetailReportRequest request);
    SalesReportResponse generateSalesReportRevenue(SalesDetailReportRequest request);
    SalesReportResponse generateSalesReportByQuater(SalesRevenueQuarterRequest request);
    List<SalesQuarterResponse> allQuarterReport(int year);
    SalesReportResponse salesRevenueByRegion(SalesRevenueByRegionRequest request);

}