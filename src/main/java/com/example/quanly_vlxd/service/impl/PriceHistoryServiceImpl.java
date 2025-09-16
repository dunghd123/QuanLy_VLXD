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
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<MessageResponse> addPrice(PriceHistoryRequest priceHistoryRequest) {
        Date date = new Date();
        Optional<Product> product = productRepo.findById(priceHistoryRequest.getProductId());
        if(product.isEmpty()){
            return ResponseEntity.badRequest().body(MessageResponse.builder().message("Product ID: "+ priceHistoryRequest.getProductId()+" is not exist").build());
        }

        Optional<ProductPriceHistory> activePrice = priceHistoryRepo.findActivePriceByProductId(priceHistoryRequest.getProductId(), priceHistoryRequest.getInvoiceType());
        if(activePrice.isPresent()){
            ProductPriceHistory currentPriceActive = activePrice.get();
            currentPriceActive.setEndDate(date);
            currentPriceActive.setActive(false);
            priceHistoryRepo.save(currentPriceActive);
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
        return ResponseEntity.ok(MessageResponse.builder().message("Create price history successfully").build());
    }

    @Override
    public Page<PriceHistoryResponse> filter(PriceFilterRequest filter) {
        Sort sort = (filter.getPriceTypeFilter() == PriceTypeEnums.CURRENT)
                ? Sort.by(Sort.Order.desc("startDate"))
                : Sort.by(Sort.Order.desc("endDate"));

        Pageable pageable = PageRequest.of(filter.getPageFilter(), filter.getSizeFilter(), sort);

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
            Date start = parseDate(filter.getStartDateFilter());
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("startDate"), start));
        }

        if (filter.getEndDateFilter() != null) {
            Date end = parseDate(filter.getEndDateFilter());
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("endDate"), end));
        }

        boolean isActive = filter.getPriceTypeFilter() == PriceTypeEnums.CURRENT;
        spec = spec.and((root, query, cb) -> cb.equal(root.get("isActive"), isActive));

        return priceHistoryRepo.findAll(spec, pageable)
                .map(this::convertToDTO);
    }
    private Date parseDate(String dateStr) {
        String[] patterns = { "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss" };
        for (String pattern : patterns) {
            try {
                return new SimpleDateFormat(pattern).parse(dateStr);
            } catch (ParseException ignored) {}
        }
        throw new RuntimeException("Invalid date format, must be yyyy-MM-dd'T'HH:mm:ss or yyyy-MM-dd HH:mm:ss");
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
