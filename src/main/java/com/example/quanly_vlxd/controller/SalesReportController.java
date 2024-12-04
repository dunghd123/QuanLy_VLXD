package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.request.SalesDetailReportRequest;
import com.example.quanly_vlxd.dto.response.SalesDetailResponse;
import com.example.quanly_vlxd.dto.response.SalesReportResponse;
import com.example.quanly_vlxd.service.SalesReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sales-reports/")
public class SalesReportController {

    @Autowired
    private SalesReportService salesReportService;

    @GetMapping("sales-report-detailed")
    public List<SalesDetailResponse> generateSalesReportDetailed(@RequestBody SalesDetailReportRequest request) {
        return salesReportService.generateSalesReportDetailed(request);
    }

    // Phương thức tạo thống kê doanh thu
    @GetMapping("sales-report-revenue")
    public SalesReportResponse generateSalesReportRevenue(@RequestBody SalesDetailReportRequest request) {
       return salesReportService.generateSalesReportRevenue(request);
    }
}