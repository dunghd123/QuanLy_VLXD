package com.example.quanly_vlxd.service;


import com.example.quanly_vlxd.dto.request.PriceFilterRequest;
import com.example.quanly_vlxd.dto.request.PriceHistoryRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.PriceHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface PriceHistoryService {
    ResponseEntity<MessageResponse> addPrice(PriceHistoryRequest priceHistoryRequest);

    Page<PriceHistoryResponse> filter(PriceFilterRequest req);
}
