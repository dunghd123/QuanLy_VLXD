package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.request.PriceHistoryRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.entity.ProductPriceHistory;
import com.example.quanly_vlxd.service.impl.PriceHistoryServiceImpl;
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
public class PriceHistoryController {
    @Autowired
    private  PriceHistoryServiceImpl priceHistoryService;


    @PostMapping("create-price-history")
    public ResponseEntity<MessageResponse> createPriceHistory(@Valid @RequestBody PriceHistoryRequest priceHistoryRequest) {
        return new ResponseEntity<>(priceHistoryService.addPrice(priceHistoryRequest), HttpStatus.CREATED);
    }

    @GetMapping("getAll")
    public Page<ProductPriceHistory> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return priceHistoryService.getAll(page, size);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Trả về mã 400 BAD_REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class) // Xử lý ngoại lệ MethodArgumentNotValidException
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getMapErrors(ex);
    }
}
