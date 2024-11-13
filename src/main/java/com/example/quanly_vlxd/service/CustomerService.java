package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.CategoryDTO;
import com.example.quanly_vlxd.dto.CustomerDTO;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.entity.Category;
import com.example.quanly_vlxd.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    MessageResponse addCustomer(CustomerDTO customerDTO);
    MessageResponse updateCustomer(int id, CustomerDTO customerDTO);

    MessageResponse deleteCustomer(int id);
    Page<Customer> getAllCustomer(int page, int size);
}
