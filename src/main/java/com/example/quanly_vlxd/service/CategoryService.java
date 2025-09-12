package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.request.CategoryRequest;
import com.example.quanly_vlxd.dto.response.CategoryResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    ResponseEntity<MessageResponse> addCategory(CategoryRequest categoryRequest);
    ResponseEntity<MessageResponse> updateCategory(int id, CategoryRequest categoryRequest);

    ResponseEntity<MessageResponse> deleteCategory(int id);
    Page<CategoryResponse> getAll(int page, int size);
    List<CategoryResponse> getListCategory();
}
