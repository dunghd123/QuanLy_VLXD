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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    @Override
    public MessageResponse addProduct(ProductRequest productRequest) {
        Optional<Category> categoryOpt = categoryRepo.findById(productRequest.getCategoryId());
        if (categoryOpt.isEmpty()) {
            return MessageResponse.builder().message("Category ID: " + productRequest.getCategoryId() + " does not exist").build();
        }

        Product newProduct = Product.builder()
                .Name(productRequest.getName())
                .UnitMeasure(productRequest.getUnitMeasure())
                .Description(productRequest.getDescription())
                .IsActive(true)
                .category(categoryOpt.get()) // Thiết lập danh mục cho sản phẩm
                .build();

        productRepo.save(newProduct);
        return MessageResponse.builder().message("Create new product successfully!!!").build();
    }

    @Override
    public MessageResponse updateProduct(int id, ProductRequest productRequest) {
        Optional<Product> productOpt = productRepo.findById(id);
        if (productOpt.isEmpty()) {
            return MessageResponse.builder().message("Product ID: " + id + " does not exist").build();
        }

        Product product = productOpt.get();
        Optional<Category> categoryOpt = categoryRepo.findById(productRequest.getCategoryId());
        if (categoryOpt.isEmpty()) {
            return MessageResponse.builder().message("Category ID: " + productRequest.getCategoryId() + " does not exist").build();
        }

        product.setName(productRequest.getName());
        product.setUnitMeasure(productRequest.getUnitMeasure());
        product.setDescription(productRequest.getDescription());
        product.setCategory(categoryOpt.get()); // Cập nhật danh mục cho sản phẩm

        productRepo.save(product);
        return MessageResponse.builder().message("Update product information successfully!!!").build();
    }

    @Override
    public MessageResponse deleteProduct(int id) {
        Optional<Product> productOpt = productRepo.findById(id);
        if (productOpt.isEmpty()) {
            return MessageResponse.builder().message("Product ID: " + id + " does not exist").build();
        }

        Product product = productOpt.get();
        product.setIsActive(false); // Thực hiện soft delete thay vì xóa hoàn toàn
        productRepo.save(product);

        return MessageResponse.builder().message("Delete product ID: " + id + " successfully!!!").build();
    }

    @Override
    public Page<ProductResponse> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepo.getAll(pageable).map(this::convertToDTO);
    }
    private ProductResponse convertToDTO(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .cate_id(product.getCategory().getId())
                .unitMeasure(product.getUnitMeasure())
                .description(product.getDescription())
                .isActive(product.isIsActive())
                .build();
    }
}
