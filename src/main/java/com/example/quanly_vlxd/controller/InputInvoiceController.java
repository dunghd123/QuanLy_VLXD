package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.request.InputFilterRequest;
import com.example.quanly_vlxd.dto.request.InputInvoiceRequest;
import com.example.quanly_vlxd.dto.response.InputInvoiceResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.enums.InvoiceStatusEnums;
import com.example.quanly_vlxd.service.impl.InputInvoiceServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.quanly_vlxd.help.MapErrors.getMapErrors;

@RestController
@RequestMapping("/api/v1/input-invoice/")
@CrossOrigin("*")
@RequiredArgsConstructor
public class InputInvoiceController {
    private final InputInvoiceServiceImpl inputInvoiceService;

    @PostMapping("add-input-invoice")
    public ResponseEntity<MessageResponse> addInputInvoice(@Valid @RequestBody InputInvoiceRequest inputInvoiceRequest){
        return inputInvoiceService.addInputInvoice(inputInvoiceRequest);
    }
    @PutMapping("update-input-invoice/{id}")
    public ResponseEntity<MessageResponse> updateInputInvoice(@PathVariable(value = "id") int id, @Valid @RequestBody InputInvoiceRequest inputInvoiceRequest){
        return inputInvoiceService.updateInputInvoice(id,inputInvoiceRequest);
    }
    @DeleteMapping("delete-input-invoice/{id}")
    public ResponseEntity<MessageResponse> deleteInputInvoice(@PathVariable(value = "id") int id){
        return inputInvoiceService.deleteInputInvoice(id);
    }

    @PutMapping("update-unitprice")
    public void updateUnitPrice(){
        inputInvoiceService.updateUnitPrice();
    }
    @GetMapping("find-input-invoice/{id}")
    public ResponseEntity<InputInvoiceResponse> find(@PathVariable(value = "id") int id){
        InputInvoiceResponse inputInvoiceResponse = inputInvoiceService.getInputInvoice(id);
        if(inputInvoiceResponse == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(inputInvoiceResponse, HttpStatus.OK);
    }
    @GetMapping("getAllInputInvoiceByEmp/{username}")
    public ResponseEntity<Page<InputInvoiceResponse>> getAllInputInvoiceByEmp(
            @PathVariable(value = "username") String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String supName,
            @RequestParam(required = false) String status){
        InputFilterRequest filter= new InputFilterRequest();
        filter.setPageFilter(page);
        filter.setSizeFilter(size);
        filter.setSupNameFilter(supName);
        filter.setStatusFilter(status != null ? Enum.valueOf(InvoiceStatusEnums.class, status.toUpperCase()) : null);
        return ResponseEntity.ok(inputInvoiceService.getAllInputInvoiceByEmp(filter, username));
    }
    @GetMapping("getAllPendingInputInvoiceByEmp/{username}")
    public ResponseEntity<Page<InputInvoiceResponse>> getAllPendingInputInvoiceByEmp(
            @PathVariable(value = "username") String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(inputInvoiceService.getAllPendingInputInvoiceByEmp(page, size, username));
    }
    @PutMapping("approve-input-invoice/{id}")
    public ResponseEntity<MessageResponse> approveInputInvoice(@PathVariable(value = "id") int id){
        return inputInvoiceService.approveInputInvoice(id);
    }
    @PutMapping("reject-input-invoice/{id}")
    public ResponseEntity<MessageResponse> rejectInputInvoice(@PathVariable(value = "id") int id){
        return inputInvoiceService.rejectInputInvoice(id);
    }
    @PutMapping("complete-input-invoice/{id}")
    public ResponseEntity<MessageResponse> completeInputInvoice(@PathVariable(value = "id") int id){
        return inputInvoiceService.completeInputInvoice(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // Trả về mã 400 BAD_REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class) // Xử lý ngoại lệ MethodArgumentNotValidException
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getMapErrors(ex);
    }
}
