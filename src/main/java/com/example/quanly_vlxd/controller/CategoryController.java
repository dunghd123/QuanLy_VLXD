package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.request.CategoryRequest;
import com.example.quanly_vlxd.dto.response.CategoryResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.service.impl.CategoryServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.quanly_vlxd.help.MapErrors.getMapErrors;

@RestController
@RequestMapping("/api/v1/category/")
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryService;
    @PostMapping("add-category")
    public ResponseEntity<MessageResponse> addNewCategory(@Valid @RequestBody CategoryRequest categoryRequest){
        return new ResponseEntity<>(categoryService.addCategory(categoryRequest), HttpStatus.CREATED);
    }
    @PutMapping("update-category")
    public ResponseEntity<MessageResponse> updateCategory(@RequestParam int id,@Valid @RequestBody CategoryRequest categoryRequest){
        return new ResponseEntity<>(categoryService.updateCategory(id, categoryRequest), HttpStatus.OK);
    }
    @DeleteMapping("delete-category")
    public ResponseEntity<MessageResponse> deleteCategory(@RequestParam int id){
        return new ResponseEntity<>(categoryService.deleteCategory(id), HttpStatus.OK);
    }
    @GetMapping("getAll")
    public Page<CategoryResponse> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return categoryService.getAll(page,size);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // Trả về mã 400 BAD_REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class) // Xử lý ngoại lệ MethodArgumentNotValidException
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getMapErrors(ex);
    }


}
