package com.example.quanly_vlxd.service;


import com.example.quanly_vlxd.dto.PriceHistoryDTO;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.entity.Category;
import com.example.quanly_vlxd.entity.ProductPriceHistory;
import org.springframework.data.domain.Page;

public interface PriceHistoryService {
    MessageResponse addPrice(PriceHistoryDTO priceHistoryDTO);

    Page<ProductPriceHistory> getAll(int page, int size);
}
