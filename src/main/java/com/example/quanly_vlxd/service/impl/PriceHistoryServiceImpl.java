package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.dto.PriceHistoryDTO;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.entity.Product;
import com.example.quanly_vlxd.entity.ProductPriceHistory;
import com.example.quanly_vlxd.enums.InvoiceTypeEnums;
import com.example.quanly_vlxd.repo.PriceHistoryRepo;
import com.example.quanly_vlxd.repo.ProductRepo;
import com.example.quanly_vlxd.service.PriceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PriceHistoryServiceImpl implements PriceHistoryService {
    @Autowired
    private   PriceHistoryRepo priceHistoryRepo;
    @Autowired
    private ProductRepo productRepo;

    @Override
    public MessageResponse addPrice(PriceHistoryDTO priceHistoryDTO) {
        Optional<Product> product = productRepo.findById(priceHistoryDTO.getProductId());
        if(product.isEmpty()){
            return MessageResponse.builder().message("Product ID: "+priceHistoryDTO.getProductId()+" is not exist").build();
        }

        Optional<ProductPriceHistory> activePrice = priceHistoryRepo.findActivePriceByProductId(priceHistoryDTO.getProductId(),priceHistoryDTO.getInvoiceType());
        if(activePrice.isPresent()){
            if(priceHistoryDTO.getStartDate().before(activePrice.get().getStartDate())){
                return MessageResponse.builder().message("Start date must be after current active price").build();
            }
            ProductPriceHistory currentActive = activePrice.get();
            currentActive.setEndDate(priceHistoryDTO.getStartDate());
            currentActive.setIsActive(false);
            priceHistoryRepo.save(currentActive);
        }
        ProductPriceHistory newPrice =
                ProductPriceHistory.builder()
                        .product(product.get())
                        .Price(priceHistoryDTO.getPrice())
                        .InvoiceType(InvoiceTypeEnums.valueOf(priceHistoryDTO.getInvoiceType()))
                        .StartDate(priceHistoryDTO.getStartDate())
                        .EndDate(null)
                        .IsActive(true)
                        .build();
        priceHistoryRepo.save(newPrice);
        return MessageResponse.builder().message("Create price history successfully").build();
    }



    @Override
    public Page<ProductPriceHistory> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return priceHistoryRepo.getAll(pageable);
    }
}
