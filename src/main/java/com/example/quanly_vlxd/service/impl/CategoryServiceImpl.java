package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.dto.request.CategoryRequest;
import com.example.quanly_vlxd.dto.response.CategoryResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.entity.Category;
import com.example.quanly_vlxd.entity.InputInvoiceDetail;
import com.example.quanly_vlxd.entity.Product;
import com.example.quanly_vlxd.entity.WareHouse_Product;
import com.example.quanly_vlxd.repo.CategoryRepo;
import com.example.quanly_vlxd.repo.InputInvoiceDetailRepo;
import com.example.quanly_vlxd.repo.ProductRepo;
import com.example.quanly_vlxd.repo.WarehouseProductRepo;
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
    public MessageResponse addCategory(CategoryRequest categoryRequest) {
        for(Category category: categoryRepo.findAll()){
            if(category.getName().equals(categoryRequest.getName())){
                return MessageResponse.builder().message("Category name already exist!!!").build();
            }
        }
        Category newCate= Category.builder()
                .Name(categoryRequest.getName())
                .Description(categoryRequest.getDescription())
                .IsActive(true)
                .build();
        categoryRepo.save(newCate);
        return MessageResponse.builder().message("create new category successfully!!!").build();
    }

    @Override
    public MessageResponse updateCategory(int id, CategoryRequest categoryRequest) {
        Optional<Category> category= categoryRepo.findById(id);
        if(category.isEmpty()){
            return MessageResponse.builder().message("ID: "+id+ " is not exist").build();
        }
        List<Category> list= new ArrayList<>(categoryRepo.findAll());
        list.removeIf(c->c.getId()==id);
        for(Category c: list){
            if(c.getName().equals(categoryRequest.getName())){
                return MessageResponse.builder().message("Category name already exist!!!").build();
            }
        }
        Category curCategory= category.get();
        curCategory.setName(categoryRequest.getName());
        curCategory.setDescription(categoryRequest.getDescription());
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
                for(WareHouse_Product inv: warehouseProductRepo.findAll()){
                    if(inv.getProduct().getId()== pr.getId()){
                        inv.setIsActive(false);
                        warehouseProductRepo.save(inv);
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
    public Page<CategoryResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryRepo.getAll(pageable).map(this::convertToDTO);
    }
    private CategoryResponse convertToDTO(Category category){
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .isActive(category.isIsActive())
                .build();
    }
}
