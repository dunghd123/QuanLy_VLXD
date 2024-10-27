package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.InputInvoiceDTO;
import com.example.quanly_vlxd.dto.response.MessageResponse;

public interface InputInVoiceService {
     public MessageResponse addNewInputInvoice(InputInvoiceDTO inputInvoiceDTO);
    public MessageResponse updateInputInvoice(int id, InputInvoiceDTO inputInvoiceDTO);
    public MessageResponse deleteInputInvoice(int id);

}
