package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.request.SalesDetailReportRequest;
import com.example.quanly_vlxd.dto.request.SalesRevenueQuaterRequest;
import com.example.quanly_vlxd.dto.response.SalesDetailResponse;
import com.example.quanly_vlxd.dto.response.SalesQuaterResponse;
import com.example.quanly_vlxd.dto.response.SalesReportResponse;
import com.example.quanly_vlxd.service.SalesReportService;
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
    @GetMapping("sales-report-by-quater")
        public SalesReportResponse generateSalesReportByQuater(@Valid @RequestBody SalesRevenueQuaterRequest request) {
        return salesReportService.generateSalesReportByQuater(request);
    }
    @GetMapping("total-revenue-by-quater")
    public List<SalesQuaterResponse> generateTotalRevenueAllQuater(@RequestParam int year) {
        return salesReportService.allQuarterReport(year);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Trả về mã 400 BAD_REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class) // Xử lý ngoại lệ MethodArgumentNotValidException
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getMapErrors(ex);
    }
}