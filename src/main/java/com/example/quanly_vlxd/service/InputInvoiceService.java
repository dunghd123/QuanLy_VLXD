package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.request.InputInvoiceRequest;
import com.example.quanly_vlxd.dto.response.InputInvoiceResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;

public interface InputInvoiceService {

    MessageResponse addInputInvoice(InputInvoiceRequest inputInvoiceRequest);
    MessageResponse updateInputInvoice(int id);

    MessageResponse deleteInputInvoice(int id);

    InputInvoiceResponse getInputInvoice(int id);


}
