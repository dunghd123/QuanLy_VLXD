package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.SupplierDTO;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.entity.Supplier;
import com.example.quanly_vlxd.service.impl.SupplierServiceImpl;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/supplier/")
public class SupplierController {
    @Autowired
    private SupplierServiceImpl supplierService;
    @PostMapping("add-supplier")
    public ResponseEntity<MessageResponse> addNewSupplier(@Valid @RequestBody SupplierDTO supplierDTO){
        return new ResponseEntity<>(supplierService.addSupplier(supplierDTO), HttpStatus.CREATED);
    }
    @PutMapping("update-supplier")
    public ResponseEntity<MessageResponse> updateSupplier(@RequestParam int id,@Valid @RequestBody SupplierDTO supplierDTO){
        return new ResponseEntity<>(supplierService.updateSupplier(id,supplierDTO), HttpStatus.OK);
    }
    @DeleteMapping("delete-supplier")
    public ResponseEntity<MessageResponse> deleteSupplier(@RequestParam int id){
        return new ResponseEntity<>(supplierService.deleteSuppler(id), HttpStatus.OK);
    }
    @GetMapping("getAll")
    public Page<Supplier> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return supplierService.getList(page,size);
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST) // Trả về mã 400 BAD_REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class) // Xử lý ngoại lệ MethodArgumentNotValidException
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
