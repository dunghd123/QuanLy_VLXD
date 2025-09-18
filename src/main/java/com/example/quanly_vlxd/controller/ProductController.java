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

import java.util.List;
import java.util.Map;

import static com.example.quanly_vlxd.help.MapErrors.getMapErrors;

@RestController
@RequestMapping("/api/v1/product/")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @PostMapping("add-product")
    public ResponseEntity<MessageResponse> addNewProduct(@Valid @RequestBody ProductRequest productRequest) {
        return productService.addProduct(productRequest);
    }

    @PutMapping("update-product/{id}")
    public ResponseEntity<MessageResponse> updateProduct(@PathVariable(value = "id") int id, @Valid @RequestBody ProductRequest productRequest) {
        return productService.updateProduct(id, productRequest);
    }

    @DeleteMapping("delete-product/{id}")
    public ResponseEntity<MessageResponse> deleteProduct(@PathVariable(value = "id") int id) {
        return productService.deleteProduct(id);
    }

    @GetMapping("getAllProduct")
    public Page<ProductResponse> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return productService.getAllProducts(page, size);
    }

    @GetMapping("getListActiveProducts")
    public List<ProductResponse> getListActiveProduct() {
        return productService.getListActiveProduct();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // Trả về mã 400 BAD_REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class) // Xử lý ngoại lệ MethodArgumentNotValidException
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getMapErrors(ex);
    }
}
