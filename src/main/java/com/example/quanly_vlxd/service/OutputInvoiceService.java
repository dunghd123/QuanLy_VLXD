package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.request.OutputFilterRequest;
import com.example.quanly_vlxd.dto.request.OutputInvoiceRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.OutputInvoiceResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface OutputInvoiceService {
    ResponseEntity<MessageResponse> addOutputInvoice(OutputInvoiceRequest outputInvoiceRequest);
    ResponseEntity<MessageResponse> approveOutputInvoice(int id);
    ResponseEntity<MessageResponse> CompleteOutputInvoice(int id);
    ResponseEntity<MessageResponse> rejectOutputInvoice(int id);
    ResponseEntity<MessageResponse> updateOutputInvoice(int id, OutputInvoiceRequest outputInvoiceRequest);
    ResponseEntity<MessageResponse> deleteOutputInvoice(int id);
    Page<OutputInvoiceResponse> getAllOutputInvoiceByEmp(OutputFilterRequest outputFilterRequest, String username);

}
