package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.dto.request.CustomerRequest;
import com.example.quanly_vlxd.dto.response.CusResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.entity.Customer;
import com.example.quanly_vlxd.entity.OutputInvoice;
import com.example.quanly_vlxd.repo.CustomerRepo;
import com.example.quanly_vlxd.repo.OutputInvoiceDetailRepo;
import com.example.quanly_vlxd.repo.OutputInvoiceRepo;
import com.example.quanly_vlxd.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {


    private final CustomerRepo customerRepo;


    private final OutputInvoiceRepo outputInvoiceRepo;

    @Autowired
    public CustomerServiceImpl(CustomerRepo customerRepo, OutputInvoiceDetailRepo outputInvoiceDetailRepo, OutputInvoiceRepo outputInvoiceRepo) {
        this.customerRepo = customerRepo;
        this.outputInvoiceRepo = outputInvoiceRepo;
    }
    @Override
    public ResponseEntity<MessageResponse> addCustomer(CustomerRequest customerRequest) {
        boolean isPhoneExist = customerRepo.existsByPhoneNum(customerRequest.getPhone());
        if(isPhoneExist){
            return  ResponseEntity.badRequest().body(MessageResponse.builder().message("Customer phone number already exist!!!").build());
        }
        Customer newCus = Customer.builder()
                .name(customerRequest.getName())
                .address(customerRequest.getAddress())
                .phoneNum(customerRequest.getPhone())
                .isActive(true)
                .build();
        customerRepo.save(newCus);
        return ResponseEntity.ok(MessageResponse.builder().message("Create new customer successfully!!!").build());
    }

    @Override
    public ResponseEntity<MessageResponse> updateCustomer(int id, CustomerRequest customerRequest) {
        Optional<Customer> customer = customerRepo.findById(id);
        if(customer.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Customer not found"));
        }
        boolean isPhoneExist = customerRepo.existsByPhoneNumAndIdNot(customerRequest.getPhone(), id);
        if(isPhoneExist){
            return ResponseEntity.badRequest().body(MessageResponse.builder().message("Customer phone number already exist!!!").build());
        }
        Customer customerUpdate= customer.get();
        customerUpdate.setName(customerRequest.getName());
        customerUpdate.setAddress(customerRequest.getAddress());
        customerUpdate.setPhoneNum(customerRequest.getPhone());
        customerRepo.save(customerUpdate);
        return ResponseEntity.ok(MessageResponse.builder().message("Update customer successfully!!!").build());
    }

    @Override
    public ResponseEntity<MessageResponse> deleteCustomer(int id) {
        Optional<Customer> customer = customerRepo.findById(id);
        if(customer.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Customer not found"));
        }
        for (OutputInvoice outputInvoice : outputInvoiceRepo.findAll()) {
            if (outputInvoice.getCustomer().getId() == id) {
                outputInvoice.setActive(false);
                outputInvoiceRepo.save(outputInvoice);
            }
        }
        customer.get().setActive(false);
        customerRepo.save(customer.get());
        return ResponseEntity.ok(MessageResponse.builder().message("Delete customer successfully!!!").build());
    }
    private static String convertName(String name){
        List<String> set = new ArrayList<>();
        String[] words = name.split(" ");
        for (String w : words) {
            w = w.substring(0, 1).toUpperCase() + w.substring(1).toLowerCase();
            set.add(w);
        }
        return String.join(" ", set);
    }
    public void setLowerName() {
        List<Customer> customers = customerRepo.findAll();
        for(Customer customer: customers){
            customer.setName(convertName(customer.getName()));
        }
        customerRepo.saveAll(customers);
    }

    @Override
    public Page<CusResponse> getAllCustomer(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return customerRepo.getAll(pageable).map(this::convertToDTO);
    }

    @Override
    public List<CusResponse> getAllActiveCustomer() {
        return customerRepo.getAllActiveCustomer().stream().map(this::convertToDTO).toList();
    }

    private CusResponse convertToDTO(Customer customer) {
        return CusResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .address(customer.getAddress())
                .phone(customer.getPhoneNum())
                .build();
    }
}
