package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.request.InputInvoiceRequest;
import com.example.quanly_vlxd.dto.response.InputInvoiceResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.service.impl.InputInvoiceServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.quanly_vlxd.help.MapErrors.getMapErrors;

@RestController
@RequestMapping("/api/v1/input-invoice/")
@RequiredArgsConstructor
public class InputInvoiceController {
    private final InputInvoiceServiceImpl inputInvoiceService;

    @PostMapping("add-input-invoice")
    public ResponseEntity<MessageResponse> addInputInvoice(@Valid @RequestBody InputInvoiceRequest inputInvoiceRequest){
        return new ResponseEntity<>(inputInvoiceService.addInputInvoice(inputInvoiceRequest), HttpStatus.CREATED);
    }
    @PutMapping("update-input-invoice/{id}")
    public ResponseEntity<MessageResponse> updateInputInvoice(@PathVariable(value = "id") int id){
        return new ResponseEntity<>(inputInvoiceService.updateInputInvoice(id), HttpStatus.OK);
    }
    @DeleteMapping("delete-input-invoice/{id}")
    public ResponseEntity<MessageResponse> deleteInputInvoice(@PathVariable(value = "id") int id){
        return new ResponseEntity<>(inputInvoiceService.deleteInputInvoice(id), HttpStatus.OK);
    }
    @GetMapping("find-input-invoice/{id}")
    public InputInvoiceResponse find(@PathVariable(value = "id") int id){
        return inputInvoiceService.getInputInvoice(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // Trả về mã 400 BAD_REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class) // Xử lý ngoại lệ MethodArgumentNotValidException
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getMapErrors(ex);
    }
}
