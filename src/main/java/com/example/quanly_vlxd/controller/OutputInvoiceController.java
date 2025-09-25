package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.request.OutputFilterRequest;
import com.example.quanly_vlxd.dto.request.OutputInvoiceRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.OutputInvoiceResponse;
import com.example.quanly_vlxd.enums.InvoiceStatusEnums;
import com.example.quanly_vlxd.service.impl.OutputInvoiceServiceImpl;
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
@RequestMapping("/api/v1/output-invoice/")
@CrossOrigin("*")
@RequiredArgsConstructor
public class OutputInvoiceController {
    private final OutputInvoiceServiceImpl outputInvoiceService;

    @PostMapping("add-output-invoice")
    public ResponseEntity<MessageResponse> addOutputInvoice(@Valid @RequestBody OutputInvoiceRequest outputInvoiceRequest){
        return outputInvoiceService.addOutputInvoice(outputInvoiceRequest);
    }

    @PutMapping("update-output-invoice/{id}")
    public ResponseEntity<MessageResponse> updateOutputInvoice(@PathVariable(value = "id") int id, @Valid @RequestBody OutputInvoiceRequest outputInvoiceRequest){
        return outputInvoiceService.updateOutputInvoice(id, outputInvoiceRequest);
    }

    @DeleteMapping("delete-output-invoice/{id}")
    public ResponseEntity<MessageResponse> deleteOutputInvoice(@PathVariable(value = "id") int id){
        return outputInvoiceService.deleteOutputInvoice(id);
    }

    @GetMapping("getAllOutputInvoiceByEmp/{username}")
    public ResponseEntity<Page<OutputInvoiceResponse>> getAllOutputInvoiceByEmp(
            @PathVariable(value = "username") String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String cusName,
            @RequestParam(required = false) String status){
        OutputFilterRequest filter= new OutputFilterRequest();
        filter.setPageFilter(page);
        filter.setSizeFilter(size);
        filter.setCusNameFilter(cusName);
        filter.setStatusFilter(status != null ? Enum.valueOf(InvoiceStatusEnums.class, status.toUpperCase()) : null);
        return ResponseEntity.ok(outputInvoiceService.getAllOutputInvoiceByEmp(filter,username));
    }

    @GetMapping("getAllPendingOutputInvoiceByEmp/{username}")
    public ResponseEntity<Page<OutputInvoiceResponse>> getAllPendingOutputInvoiceByEmp(
            @PathVariable(value = "username") String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(outputInvoiceService.getAllPendingOutputInvoiceByEmp(page, size, username));
    }
    @PutMapping("approve-output-invoice/{id}")
    public ResponseEntity<MessageResponse> approveOutputInvoice(@PathVariable(value = "id") int id){
        return outputInvoiceService.approveOutputInvoice(id);
    }
    @PutMapping("reject-output-invoice/{id}")
    public ResponseEntity<MessageResponse> rejectOutputInvoice(@PathVariable(value = "id") int id){
        return outputInvoiceService.rejectOutputInvoice(id);
    }
    @PutMapping("complete-output-invoice/{id}")
    public ResponseEntity<MessageResponse> completeOutputInvoice(@PathVariable(value = "id") int id){
        return outputInvoiceService.completeOutputInvoice(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getMapErrors(ex);
    }
}
