package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.request.OutputInvoiceRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.OutputInvoiceResponse;
import com.example.quanly_vlxd.service.impl.OutputInvoiceServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.quanly_vlxd.help.MapErrors.getMapErrors;

@RestController
@RequestMapping("/api/v1/output-invoice/")
@RequiredArgsConstructor
public class OutputInvoiceController {
    private final OutputInvoiceServiceImpl outputInvoiceService;

    @PostMapping("add-output-invoice")
    public ResponseEntity<MessageResponse> addOutputInvoice(@Valid @RequestBody OutputInvoiceRequest outputInvoiceRequest){
        return new ResponseEntity<>(outputInvoiceService.addOutputInvoice(outputInvoiceRequest), HttpStatus.CREATED);
    }

    @PutMapping("update-output-invoice/{id}")
    public ResponseEntity<MessageResponse> updateOutputInvoice(@PathVariable(value = "id") int id){
        return new ResponseEntity<>(outputInvoiceService.updateOutputInvoice(id), HttpStatus.OK);
    }

    @DeleteMapping("delete-output-invoice/{id}")
    public ResponseEntity<MessageResponse> deleteOutputInvoice(@PathVariable(value = "id") int id){
        return new ResponseEntity<>(outputInvoiceService.deleteOutputInvoice(id), HttpStatus.OK);
    }

    @GetMapping("find-output-invoice/{id}")
    public ResponseEntity<OutputInvoiceResponse> findOutputInvoice(@PathVariable(value = "id") int id){
        OutputInvoiceResponse outputInvoiceResponse = outputInvoiceService.findOutputInvoice(id);
        if(outputInvoiceResponse == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(outputInvoiceResponse, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // Trả về mã 400 BAD_REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class) // Xử lý ngoại lệ MethodArgumentNotValidException
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getMapErrors(ex);
    }
}
