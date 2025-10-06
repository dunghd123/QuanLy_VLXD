package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.request.SalesDetailReportRequest;
import com.example.quanly_vlxd.dto.response.SaleReportResponse;
import com.example.quanly_vlxd.service.SalesReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.quanly_vlxd.help.DateConvert.convertStringToDate;
import static com.example.quanly_vlxd.help.MapErrors.getMapErrors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sales-reports/")
public class SalesReportController {
    private final SalesReportService salesReportService;
    @PostMapping("revenue-report")
    public ResponseEntity<SaleReportResponse> getRevenue(@RequestBody SalesDetailReportRequest request) {

        return ResponseEntity.ok(salesReportService.getRevenueInDate(convertStringToDate(request.getStartDate()), convertStringToDate(request.getEndDate()), request.getTypeReport()));
    }
    @PostMapping("revenue-report-by-month")
    public ResponseEntity<SaleReportResponse> getRevenueByMonth(@RequestParam int year) {
        return ResponseEntity.ok(salesReportService.getRevenueInMonth(year));
    }
    @PostMapping("revenue-report-by-quarter")
    public ResponseEntity<SaleReportResponse> getRevenueByQuarter(@RequestParam int year) {
        return ResponseEntity.ok(salesReportService.getRevenueInQuarter(year));
    }
    @PostMapping("revenue-report-by-region")
    public ResponseEntity<SaleReportResponse> getRevenueByRegion(@RequestParam int year) {
        return ResponseEntity.ok(salesReportService.getRegionRevenue(year));
    }
//    @GetMapping("generate-sales-report-to-Pdf-by-quater")
//    public void generateSalesReportByQuarter(@RequestParam int year) throws Exception {
//        salesReportService.generateReportQuaterToPdf(year);
//    }
//    @GetMapping("generate-sales-report-to-Pdf-by-region")
//    public void generateSalesReportByRegion() throws Exception {
//        salesReportService.generateReportRegionToPdf();
//    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getMapErrors(ex);
    }
}