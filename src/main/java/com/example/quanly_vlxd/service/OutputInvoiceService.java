package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.request.OutputInvoiceRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.OutputInvoiceResponse;

public interface OutputInvoiceService {
    MessageResponse addOutputInvoice(OutputInvoiceRequest outputInvoiceRequest);
    MessageResponse updateOutputInvoice(int id);
    MessageResponse deleteOutputInvoice(int id);
    OutputInvoiceResponse findOutputInvoice(int id);
}
