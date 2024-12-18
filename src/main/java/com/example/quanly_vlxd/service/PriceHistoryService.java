package com.example.quanly_vlxd.service;


import com.example.quanly_vlxd.dto.request.PriceHistoryRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.PriceHistoryResponse;
import org.springframework.data.domain.Page;

public interface PriceHistoryService {
    MessageResponse addPrice(PriceHistoryRequest priceHistoryRequest);

    Page<PriceHistoryResponse> getAll(int page, int size);
}
