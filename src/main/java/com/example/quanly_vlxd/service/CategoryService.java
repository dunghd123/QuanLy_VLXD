package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.request.CategoryRequest;
import com.example.quanly_vlxd.dto.response.CategoryResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import org.springframework.data.domain.Page;

public interface CategoryService {
    MessageResponse addCategory(CategoryRequest categoryRequest);
    MessageResponse updateCategory(int id, CategoryRequest categoryRequest);

    MessageResponse deleteCategory(int id);
    Page<CategoryResponse> getAll(int page, int size);
}
