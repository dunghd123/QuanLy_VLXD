
package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.response.SaleReportResponse;

import java.util.Date;

public interface SalesReportService {
    SaleReportResponse getRevenueInDate(Date start, Date end, String type);

//    List<SalesDetailResponse> generateSalesReportDetailed(SalesDetailReportRequest request);
//    SalesReportResponse generateSalesReportRevenue(SalesDetailReportRequest request);
//    SalesReportResponse generateSalesReportByQuarter(SalesRevenueQuarterRequest request);
//    List<SalesQuarterResponse> allQuarterReport(int year);
//    List<SalesMonthResponse> salesRevenueByMonth(int year);
//    SalesRevenueProductResponse salesRevenueProduct(int proId);
//    SalesReportResponse salesRevenueByRegion(SalesRevenueByRegionRequest request);
//    SalesReportResponse getTotalAmountInputInvoice(InputInvoiceReportRequest request);
//    SalesReportResponse getTotalAmountByQuater(SalesRevenueQuarterRequest request);
//    void generateReportQuaterToPdf(int year) throws Exception;
//    void generateReportRegionToPdf() throws Exception;
}