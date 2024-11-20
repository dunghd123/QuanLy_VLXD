package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.request.ProductRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {
    MessageResponse addProduct(ProductRequest productRequest);
    MessageResponse updateProduct(int id, ProductRequest productRequest);
    MessageResponse deleteProduct(int id);
    Page<ProductResponse> getAllProducts(int page, int size);

}
