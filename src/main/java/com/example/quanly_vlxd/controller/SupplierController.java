package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.request.SupplierRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.SupplierResponse;
import com.example.quanly_vlxd.service.impl.SupplierServiceImpl;
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
@RequestMapping("/api/v1/supplier/")
@CrossOrigin("*")
public class SupplierController {
    @Autowired
    private SupplierServiceImpl supplierService;
    @PostMapping("add-supplier")
    public ResponseEntity<MessageResponse> addNewSupplier(@Valid @RequestBody SupplierRequest supplierRequest){
        return supplierService.addSupplier(supplierRequest);
    }
    @PutMapping("update-supplier/{id}")
    public ResponseEntity<MessageResponse> updateSupplier(@PathVariable("id") int id,@Valid @RequestBody SupplierRequest supplierRequest){
        return supplierService.updateSupplier(id, supplierRequest);
    }
    @DeleteMapping("delete-supplier/{id}")
    public ResponseEntity<MessageResponse> deleteSupplier(@PathVariable("id") int id){
        return supplierService.deleteSuppler(id);
    }
    @GetMapping("getAllSupplier")
    public Page<SupplierResponse> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return supplierService.getList(page,size);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // Trả về mã 400 BAD_REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class) // Xử lý ngoại lệ MethodArgumentNotValidException
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getMapErrors(ex);
    }
}
