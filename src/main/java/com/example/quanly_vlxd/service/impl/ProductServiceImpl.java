package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.dto.request.ProductRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.ProductResponse;
import com.example.quanly_vlxd.entity.Category;
import com.example.quanly_vlxd.entity.Product;
import com.example.quanly_vlxd.repo.CategoryRepo;
import com.example.quanly_vlxd.repo.ProductRepo;
import com.example.quanly_vlxd.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    @Override
    public ResponseEntity<MessageResponse> addProduct(ProductRequest productRequest) {
        Optional<Category> categoryOpt = categoryRepo.findById(productRequest.getCateId());
        if (categoryOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(MessageResponse.builder().message("Category ID: " + productRequest.getCateId() + " does not exist").build());
        }
        boolean isNameExist= productRepo.existsByName(productRequest.getName());
        if(isNameExist){
            return ResponseEntity.badRequest().body(MessageResponse.builder().message("Product name already exist!!!").build());
        }

        Product newProduct = Product.builder()
                .name(productRequest.getName())
                .unitMeasure(productRequest.getUnitMeasure())
                .description(productRequest.getDescription())
                .isActive(true)
                .category(categoryOpt.get())
                .build();

        productRepo.save(newProduct);
        return ResponseEntity.ok(MessageResponse.builder().message("Create new product successfully!!!").build());
    }

    @Override
    public ResponseEntity<MessageResponse> updateProduct(int proId, ProductRequest productRequest) {
        Optional<Product> productOpt = productRepo.findById(proId);
        if (productOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(MessageResponse.builder().message("Product ID: " + proId + " does not exist").build());
        }

        Product product = productOpt.get();
        Optional<Category> categoryOpt = categoryRepo.findById(productRequest.getCateId());
        if (categoryOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(MessageResponse.builder().message("Category ID: " + productRequest.getCateId() + " does not exist").build());
        }
        boolean isNameDuplicate= productRepo.existsByNameAndIdNot(productRequest.getName(),proId);
        if(isNameDuplicate){
            return ResponseEntity.badRequest().body(MessageResponse.builder().message("Product name already exist!!!").build());
        }

        product.setName(productRequest.getName());
        product.setUnitMeasure(productRequest.getUnitMeasure());
        product.setDescription(productRequest.getDescription());
        product.setCategory(categoryOpt.get());

        productRepo.save(product);
        return ResponseEntity.ok(MessageResponse.builder().message("Update product information successfully!!!").build());
    }

    @Override
    public ResponseEntity<MessageResponse> deleteProduct(int id) {
        Optional<Product> productOpt = productRepo.findById(id);
        if (productOpt.isEmpty()) {
           return ResponseEntity.badRequest().body(MessageResponse.builder().message("Product ID: " + id + " does not exist").build());
        }

        Product product = productOpt.get();
        product.setActive(false);
        productRepo.save(product);

        return ResponseEntity.ok(MessageResponse.builder().message("Delete product ID: " + id + " successfully!!!").build());
    }

    @Override
    public Page<ProductResponse> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepo.getAll(pageable).map(this::convertToDTO);
    }

    @Override
    public List<ProductResponse> getListActiveProduct() {
        return productRepo.getListActiveProduct().stream().map(this::convertToDTO).toList();
    }

    private ProductResponse convertToDTO(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .cateName(product.getCategory().getName())
                .unitMeasure(product.getUnitMeasure())
                .description(product.getDescription())
                .build();
    }
}
