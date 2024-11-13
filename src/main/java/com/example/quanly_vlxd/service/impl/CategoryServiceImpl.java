package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.dto.CategoryDTO;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.entity.*;
import com.example.quanly_vlxd.repo.CategoryRepo;
import com.example.quanly_vlxd.repo.InputInvoiceDetailRepo;
import com.example.quanly_vlxd.repo.InventoryRepo;
import com.example.quanly_vlxd.repo.ProductRepo;
import com.example.quanly_vlxd.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private InputInvoiceDetailRepo inputInvoiceDetailRepo;
    @Override
    public MessageResponse addCategory(CategoryDTO categoryDTO) {
        for(Category category: categoryRepo.findAll()){
            if(category.getName().equals(categoryDTO.getName())){
                return MessageResponse.builder().message("Category name already exist!!!").build();
            }
        }
        Category newCate= Category.builder()
                .Name(categoryDTO.getName())
                .Description(categoryDTO.getDescription())
                .IsActive(true)
                .build();
        categoryRepo.save(newCate);
        return MessageResponse.builder().message("create new category successfully!!!").build();
    }

    @Override
    public MessageResponse updateCategory(int id, CategoryDTO categoryDTO) {
        Optional<Category> category= categoryRepo.findById(id);
        if(category.isEmpty()){
            return MessageResponse.builder().message("ID: "+id+ " is not exist").build();
        }
        Category curCategory= category.get();
        curCategory.setName(categoryDTO.getName());
        curCategory.setDescription(categoryDTO.getDescription());
        categoryRepo.save(curCategory);
        return MessageResponse.builder().message("Update information successfully!!!").build();
    }

    @Override
    public MessageResponse deleteCategory(int id) {
        Optional<Category> category= categoryRepo.findById(id);
        if(category.isEmpty()){
            return MessageResponse.builder().message("ID: "+id+ " is not exist").build();
        }
        for(Product pr: productRepo.findAll()){
            if(pr.getCategory().getId()==id){
                for(InputInvoiceDetail ip: inputInvoiceDetailRepo.findAll()){
                    if(ip.getProduct().getId()==pr.getId()){
                        inputInvoiceDetailRepo.deleteById(ip.getId());
                    }
                }
                for(WareHouse_Product inv: inventoryRepo.findAll()){
                    if(inv.getProduct().getId()== pr.getId()){
                        inv.setIsActive(false);
                        inventoryRepo.save(inv);
                    }
                }
                pr.setIsActive(false);
                productRepo.save(pr);
            }
        }
        category.get().setIsActive(false);
        categoryRepo.save(category.get());
        return MessageResponse.builder().message("Delete Category with Id:"+id+" successfully!!!").build();
    }

    @Override
    public Page<Category> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryRepo.getAll(pageable);
    }
}
