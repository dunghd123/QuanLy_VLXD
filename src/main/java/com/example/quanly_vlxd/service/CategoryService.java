package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.CategoryDTO;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.entity.Category;
import org.springframework.data.domain.Page;

public interface CategoryService {
    MessageResponse addCategory(CategoryDTO categoryDTO);
    MessageResponse updateCategory(int id, CategoryDTO categoryDTO);

    MessageResponse deleteCategory(int id);
    Page<Category> getAll(int page, int size);
}
