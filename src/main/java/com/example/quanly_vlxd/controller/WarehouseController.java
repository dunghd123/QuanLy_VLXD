package com.example.quanly_vlxd.controller;


import com.example.quanly_vlxd.dto.request.WarehouseRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.WarehouseResponse;
import com.example.quanly_vlxd.service.impl.WarehouseServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.quanly_vlxd.help.MapErrors.getMapErrors;

@RestController
@RequestMapping("/api/v1/warehouse/")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseServiceImpl warehouseService;

    @PostMapping("add-warehouse")
    public ResponseEntity<MessageResponse> addWarehouse(@Valid @RequestBody WarehouseRequest warehouseRequest) {
        return new ResponseEntity<>(warehouseService.addWarehouse(warehouseRequest), HttpStatus.CREATED);
    }
    @PutMapping("update-warehouse/{id}")
    public ResponseEntity<MessageResponse> updateWarehouse(@PathVariable(value = "id") int id,@Valid @RequestBody WarehouseRequest warehouseRequest){
        return new ResponseEntity<>(warehouseService.updateWarehouse(id, warehouseRequest), HttpStatus.OK);
    }
    @DeleteMapping("delete-warehouse/{id}")
    public ResponseEntity<MessageResponse> deleteWarehouse(@PathVariable(value = "id") int id){
        return new ResponseEntity<>(warehouseService.deleteWarehouse(id), HttpStatus.OK);
    }

    @GetMapping("getAll")
    public Page<WarehouseResponse> getList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return warehouseService.getList(page, size);
    }
    @GetMapping("getAllActiveWarehouse")
    public List<WarehouseResponse> getAllActiveWarehouse(){
        return warehouseService.getAllActiveWarehouse();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // Trả về mã 400 BAD_REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class) // Xử lý ngoại lệ MethodArgumentNotValidException
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getMapErrors(ex);
    }
}
