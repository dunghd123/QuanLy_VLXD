package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.request.PriceFilterRequest;
import com.example.quanly_vlxd.dto.request.PriceHistoryRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.PriceHistoryResponse;
import com.example.quanly_vlxd.enums.PriceTypeEnums;
import com.example.quanly_vlxd.service.PriceHistoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.quanly_vlxd.help.MapErrors.getMapErrors;

@RestController
@RequestMapping("/api/v1/price-history/")
@CrossOrigin("*")
public class PriceHistoryController {
    @Autowired
    private PriceHistoryService priceHistoryService;


    @PostMapping("create-new-price")
    public ResponseEntity<MessageResponse> createPriceHistory(@Valid @RequestBody PriceHistoryRequest priceHistoryRequest) {
        return priceHistoryService.addPrice(priceHistoryRequest);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<PriceHistoryResponse>> filterPriceHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String invoiceType,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false, name = "startdate") String startDate,
            @RequestParam(required = false, name = "enddate") String endDate,
            @RequestParam(name = "pricetype") String priceType
    ) {
        PriceFilterRequest filter = new PriceFilterRequest();
        filter.setPageFilter(page);
        filter.setSizeFilter(size);
        filter.setInvoiceTypeFilter(invoiceType);
        filter.setProductNameFilter(productName);
        filter.setStartDateFilter(startDate != null ? startDate + ":00" : null);
        filter.setEndDateFilter(endDate != null ? endDate + ":00" : null);
        filter.setPriceTypeFilter(Enum.valueOf(PriceTypeEnums.class, priceType.toUpperCase()));

        return ResponseEntity.ok(priceHistoryService.filter(filter));
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getMapErrors(ex);
    }
}
