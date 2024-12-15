package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.request.InputInvoiceReportRequest;
import com.example.quanly_vlxd.dto.request.SalesDetailReportRequest;
import com.example.quanly_vlxd.dto.request.SalesRevenueByRegionRequest;
import com.example.quanly_vlxd.dto.request.SalesRevenueQuarterRequest;
import com.example.quanly_vlxd.dto.response.*;
import com.example.quanly_vlxd.service.impl.SalesReportServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.quanly_vlxd.help.MapErrors.getMapErrors;

@RestController
@RequestMapping("/api/v1/sales-reports/")
public class SalesReportController {

    @Autowired
    private SalesReportServiceImpl salesReportService;

    @GetMapping("sales-report-detailed")
    public List<SalesDetailResponse> generateSalesReportDetailed(@RequestBody SalesDetailReportRequest request) {
        return salesReportService.generateSalesReportDetailed(request);
    }

    // Phương thức tạo thống kê doanh thu
    @GetMapping("sales-report-revenue")
    public SalesReportResponse generateSalesReportRevenue(@RequestBody SalesDetailReportRequest request) {
       return salesReportService.generateSalesReportRevenue(request);
    }
    @GetMapping("sales-report-by-quater")
        public SalesReportResponse generateSalesReportByQuarter(@Valid @RequestBody SalesRevenueQuarterRequest request) {
        return salesReportService.generateSalesReportByQuarter(request);
    }
    @GetMapping("total-revenue-by-quater")
    public List<SalesQuarterResponse> generateTotalRevenueAllQuater(@RequestParam int year) {
        return salesReportService.allQuarterReport(year);
    }
    @GetMapping("total-revenue-by-region")
    public SalesReportResponse generateTotalRevenueByRegion(@Valid @RequestBody SalesRevenueByRegionRequest request) {
        return salesReportService.salesRevenueByRegion(request);
    }
    @GetMapping("total-amount-input-invoice")
    public SalesReportResponse getTotalAmountInputInvoice(@Valid @RequestBody InputInvoiceReportRequest request) {
        return salesReportService.getTotalAmountInputInvoice(request);
    }
    @GetMapping("sales-revenue-by-month")
    public List<SalesMonthResponse> getTotalAmountByMonth(@RequestParam int year) {
        return salesReportService.salesRevenueByMonth(year);
    }
    @GetMapping("total-amount-by-quater")
    public SalesReportResponse getTotalAmountByQuarter(@Valid @RequestBody SalesRevenueQuarterRequest request) {
        return salesReportService.getTotalAmountByQuater(request);
    }
    @GetMapping("sales-revenue-by-product")
    public SalesRevenueProductResponse salesRevenueProduct(@RequestParam int proId) {
        return salesReportService.salesRevenueProduct(proId);
    }

    @GetMapping("generate-sales-report-to-Pdf-by-quater")
    public void generateSalesReportByQuarter(@RequestParam int year) throws Exception {
        salesReportService.generateReportQuaterToPdf(year);
    }
    @GetMapping("generate-sales-report-to-Pdf-by-region")
    public void generateSalesReportByRegion() throws Exception {
        salesReportService.generateReportRegionToPdf();
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Trả về mã 400 BAD_REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class) // Xử lý ngoại lệ MethodArgumentNotValidException
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getMapErrors(ex);
    }
}