package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.dto.request.PriceFilterRequest;
import com.example.quanly_vlxd.dto.request.PriceHistoryRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.PriceHistoryResponse;
import com.example.quanly_vlxd.entity.Product;
import com.example.quanly_vlxd.entity.ProductPriceHistory;
import com.example.quanly_vlxd.enums.InvoiceTypeEnums;
import com.example.quanly_vlxd.enums.PriceTypeEnums;
import com.example.quanly_vlxd.repo.PriceHistoryRepo;
import com.example.quanly_vlxd.repo.ProductRepo;
import com.example.quanly_vlxd.service.PriceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class PriceHistoryServiceImpl implements PriceHistoryService {
    @Autowired
    private   PriceHistoryRepo priceHistoryRepo;
    @Autowired
    private ProductRepo productRepo;

    @Override
    public MessageResponse addPrice(PriceHistoryRequest priceHistoryRequest) {
        Date date = new Date();
        Optional<Product> product = productRepo.findById(priceHistoryRequest.getProductId());
        if(product.isEmpty()){
            return MessageResponse.builder().message("Product ID: "+ priceHistoryRequest.getProductId()+" is not exist").build();
        }

        Optional<ProductPriceHistory> activePrice = priceHistoryRepo.findActivePriceByProductId(priceHistoryRequest.getProductId(), priceHistoryRequest.getInvoiceType());
        if(activePrice.isPresent()){
            ProductPriceHistory currentActive = activePrice.get();
            currentActive.setEndDate(date);
            currentActive.setActive(false);
            priceHistoryRepo.save(currentActive);
        }
        ProductPriceHistory newPrice =
                ProductPriceHistory.builder()
                        .product(product.get())
                        .price(priceHistoryRequest.getPrice())
                        .invoiceType(InvoiceTypeEnums.valueOf(priceHistoryRequest.getInvoiceType()))
                        .startDate(date)
                        .endDate(null)
                        .isActive(true)
                        .build();
        priceHistoryRepo.save(newPrice);
        return MessageResponse.builder().message("Create price history successfully").build();
    }

    @Override
    public Page<PriceHistoryResponse> filter(PriceFilterRequest filter) {
        Pageable pageable = PageRequest.of(filter.getPageFilter(), filter.getSizeFilter());

        Specification<ProductPriceHistory> spec = Specification.where(null);

        if (filter.getInvoiceTypeFilter() != null && !filter.getInvoiceTypeFilter().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("invoiceType"), InvoiceTypeEnums.valueOf(filter.getInvoiceTypeFilter())));
        }

        if (filter.getProductNameFilter() != null && !filter.getProductNameFilter().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("product").get("name"), "%" + filter.getProductNameFilter() + "%"));
        }

        if (filter.getStartDateFilter() != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date start = sdf.parse(filter.getStartDateFilter());
                spec = spec.and((root, query, cb) ->
                        cb.greaterThanOrEqualTo(root.get("startDate"), start));
            } catch (ParseException e) {
                throw new RuntimeException("Invalid startDate format, must be yyyy-MM-dd'T'HH:mm:ss");
            }
        }

        if (filter.getEndDateFilter() != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date end = sdf.parse(filter.getEndDateFilter());
                spec = spec.and((root, query, cb) ->
                        cb.lessThanOrEqualTo(root.get("endDate"), end));
            } catch (ParseException e) {
                throw new RuntimeException("Invalid endDate format, must be yyyy-MM-dd'T'HH:mm:ss");
            }
        }
        if(filter.getPriceTypeFilter().name().equals(PriceTypeEnums.HISTORY.name())){
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("isActive"), Boolean.FALSE));
        }else {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("isActive"), Boolean.TRUE));
        }

        return priceHistoryRepo.findAll(spec, pageable)
                .map(this::convertToDTO);
    }
    private PriceHistoryResponse convertToDTO(ProductPriceHistory productPriceHistory){
        return PriceHistoryResponse.builder()
                .id(productPriceHistory.getId())
                .productName(productPriceHistory.getProduct().getName())
                .invoiceType(productPriceHistory.getInvoiceType().name())
                .price(productPriceHistory.getPrice())
                .unitMeasure(productPriceHistory.getProduct().getUnitMeasure())
                .startDate(productPriceHistory.getStartDate())
                .endDate(productPriceHistory.getEndDate())
                .isActive(productPriceHistory.isActive())
                .build();
    }
}
