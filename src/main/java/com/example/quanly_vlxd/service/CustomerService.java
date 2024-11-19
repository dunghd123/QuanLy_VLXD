package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.request.CustomerRequest;
import com.example.quanly_vlxd.dto.response.CusResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import org.springframework.data.domain.Page;

public interface CustomerService {
    MessageResponse addCustomer(CustomerRequest customerRequest);
    MessageResponse updateCustomer(int id, CustomerRequest customerRequest);

    MessageResponse deleteCustomer(int id);
    Page<CusResponse> getAllCustomer(int page, int size);
}
