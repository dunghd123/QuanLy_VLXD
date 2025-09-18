
package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.request.InputInvoiceReportRequest;
import com.example.quanly_vlxd.dto.request.SalesDetailReportRequest;
import com.example.quanly_vlxd.dto.request.SalesRevenueByRegionRequest;
import com.example.quanly_vlxd.dto.request.SalesRevenueQuarterRequest;
import com.example.quanly_vlxd.dto.response.*;

import java.util.List;

public interface SalesReportService {

    List<SalesDetailResponse> generateSalesReportDetailed(SalesDetailReportRequest request);
    SalesReportResponse generateSalesReportRevenue(SalesDetailReportRequest request);
    SalesReportResponse generateSalesReportByQuarter(SalesRevenueQuarterRequest request);
    List<SalesQuarterResponse> allQuarterReport(int year);
    List<SalesMonthResponse> salesRevenueByMonth(int year);
    SalesRevenueProductResponse salesRevenueProduct(int proId);
    SalesReportResponse salesRevenueByRegion(SalesRevenueByRegionRequest request);
    SalesReportResponse getTotalAmountInputInvoice(InputInvoiceReportRequest request);
    SalesReportResponse getTotalAmountByQuater(SalesRevenueQuarterRequest request);
    void generateReportQuaterToPdf(int year) throws Exception;
    void generateReportRegionToPdf() throws Exception;
}