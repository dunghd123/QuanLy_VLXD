package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.request.ProductRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.ProductResponse;
import com.example.quanly_vlxd.service.impl.ProductServiceImpl;

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
@RequestMapping("/api/v1/product/")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    // Create new product
    @PostMapping("add-product")
    public ResponseEntity<MessageResponse> addNewProduct(@Valid @RequestBody ProductRequest productRequest) {
        return new ResponseEntity<>(productService.addProduct(productRequest), HttpStatus.CREATED);
    }

    // Update existing product
    @PutMapping("update-product")
    public ResponseEntity<MessageResponse> updateProduct(@RequestParam int id, @Valid @RequestBody ProductRequest productRequest) {
        return new ResponseEntity<>(productService.updateProduct(id, productRequest), HttpStatus.OK);
    }

    // Delete product
    @DeleteMapping("delete-product")
    public ResponseEntity<MessageResponse> deleteProduct(@RequestParam int id) {
        return new ResponseEntity<>(productService.deleteProduct(id), HttpStatus.OK);
    }

    // Get paginated list of products
    @GetMapping("getAll")
    public Page<ProductResponse> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return productService.getAllProducts(page, size);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // Trả về mã 400 BAD_REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class) // Xử lý ngoại lệ MethodArgumentNotValidException
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getMapErrors(ex);
    }
}
