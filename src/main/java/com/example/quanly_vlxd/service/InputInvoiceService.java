package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.request.InputFilterRequest;
import com.example.quanly_vlxd.dto.request.InputInvoiceRequest;
import com.example.quanly_vlxd.dto.response.InputInvoiceResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import org.springframework.data.domain.Page;

public interface InputInvoiceService {

    MessageResponse addInputInvoice(InputInvoiceRequest inputInvoiceRequest);
    MessageResponse updateInputInvoice(int id);
    MessageResponse deleteInputInvoice(int id);
    InputInvoiceResponse getInputInvoice(int id);
    Page<InputInvoiceResponse> getAllInputInvoiceByEmp(InputFilterRequest inputFilter, String username);
}
