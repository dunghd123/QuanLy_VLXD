package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.request.ProductRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<MessageResponse> addProduct(ProductRequest productRequest);
    ResponseEntity<MessageResponse> updateProduct(int id, ProductRequest productRequest);
    ResponseEntity<MessageResponse> deleteProduct(int id);
    Page<ProductResponse> getAllProducts(int page, int size);
    List<ProductResponse> getListActiveProduct();

}
