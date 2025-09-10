package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.request.CustomerRequest;
import com.example.quanly_vlxd.dto.response.CusResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface CustomerService {
    ResponseEntity<MessageResponse> addCustomer(CustomerRequest customerRequest);
    ResponseEntity<MessageResponse> updateCustomer(int id, CustomerRequest customerRequest);

    ResponseEntity<MessageResponse> deleteCustomer(int id);
    Page<CusResponse> getAllCustomer(int page, int size);
}
