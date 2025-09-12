package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.dto.request.CategoryRequest;
import com.example.quanly_vlxd.dto.response.CategoryResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.entity.Category;
import com.example.quanly_vlxd.entity.Product;
import com.example.quanly_vlxd.repo.CategoryRepo;
import com.example.quanly_vlxd.repo.InputInvoiceDetailRepo;
import com.example.quanly_vlxd.repo.ProductRepo;
import com.example.quanly_vlxd.repo.WarehouseProductRepo;
import com.example.quanly_vlxd.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;
    private final WarehouseProductRepo warehouseProductRepo;
    private final InputInvoiceDetailRepo inputInvoiceDetailRepo;

    @Autowired
    public CategoryServiceImpl(CategoryRepo categoryRepo, ProductRepo productRepo, WarehouseProductRepo warehouseProductRepo, InputInvoiceDetailRepo inputInvoiceDetailRepo) {
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
        this.warehouseProductRepo = warehouseProductRepo;
        this.inputInvoiceDetailRepo = inputInvoiceDetailRepo;
    }
    @Override
    public ResponseEntity<MessageResponse> addCategory(CategoryRequest categoryRequest) {
        boolean isCateNameExist= categoryRepo.existsByName(categoryRequest.getName());
        if(isCateNameExist){
            return ResponseEntity.badRequest().body(MessageResponse.builder().message("Category name already exist!!!").build());
        }
        Category newCate= Category.builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .isActive(true)
                .build();
        categoryRepo.save(newCate);
        return ResponseEntity.ok(MessageResponse.builder().message("Create new category successfully!!!").build());
    }

    @Override
    public ResponseEntity<MessageResponse> updateCategory(int id, CategoryRequest categoryRequest) {
        Optional<Category> category= categoryRepo.findById(id);
        if(category.isEmpty()){
          return ResponseEntity.badRequest().body(MessageResponse.builder().message("ID: "+id+ " is not exist").build());
        }
        boolean isCateNameDuplicate= categoryRepo.existsByNameAndIdNot(categoryRequest.getName(), id);
        if(isCateNameDuplicate){
            return ResponseEntity.badRequest().body(MessageResponse.builder().message("Category name already exist!!!").build());
        }
        Category curCategory= category.get();
        curCategory.setName(categoryRequest.getName());
        curCategory.setDescription(categoryRequest.getDescription());
        categoryRepo.save(curCategory);
        return ResponseEntity.ok(MessageResponse.builder().message("Update information successfully!!!").build());
    }

    @Override
    public ResponseEntity<MessageResponse> deleteCategory(int id) {
        Optional<Category> category= categoryRepo.findById(id);
        if(category.isEmpty()){
           return ResponseEntity.badRequest().body(MessageResponse.builder().message("ID: "+id+ " is not exist").build());
        }
        for(Product pr: productRepo.findAll()){
            if(pr.getCategory().getId()==id){
                pr.setActive(false);
                productRepo.save(pr);
            }
        }
        category.get().setActive(false);
        categoryRepo.save(category.get());
        return ResponseEntity.ok(MessageResponse.builder().message("Delete Category with Id:"+id+" successfully!!!").build());
    }

    @Override
    public Page<CategoryResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryRepo.getAll(pageable).map(this::convertToDTO);
    }

    @Override
    public List<CategoryResponse> getListCategory() {
        return categoryRepo.getListCategory().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private CategoryResponse convertToDTO(Category category){
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
