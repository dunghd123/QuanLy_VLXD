package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.request.InputFilterRequest;
import com.example.quanly_vlxd.dto.request.InputInvoiceRequest;
import com.example.quanly_vlxd.dto.response.InputInvoiceResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface InputInvoiceService {

    ResponseEntity<MessageResponse> addInputInvoice(InputInvoiceRequest inputInvoiceRequest);
    ResponseEntity<MessageResponse> approveInputInvoice(int inputInvoiceId);
    ResponseEntity<MessageResponse> completeInputInvoice(int inputInvoiceId);
    ResponseEntity<MessageResponse> rejectInputInvoice(int inputInvoiceId);
    ResponseEntity<MessageResponse> updateInputInvoice(int id, InputInvoiceRequest inputInvoiceRequest);
    ResponseEntity<MessageResponse> deleteInputInvoice(int id);
    InputInvoiceResponse getInputInvoice(int id);
    Page<InputInvoiceResponse> getAllInputInvoiceByEmp(InputFilterRequest inputFilter, String username);
}
