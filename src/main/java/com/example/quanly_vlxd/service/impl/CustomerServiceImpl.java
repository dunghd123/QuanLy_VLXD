package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.dto.request.CustomerRequest;
import com.example.quanly_vlxd.dto.response.CusResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.entity.Customer;
import com.example.quanly_vlxd.entity.OutputInvoice;
import com.example.quanly_vlxd.entity.OutputInvoiceDetail;
import com.example.quanly_vlxd.repo.CustomerRepo;
import com.example.quanly_vlxd.repo.OutputInvoiceDetailRepo;
import com.example.quanly_vlxd.repo.OutputInvoiceRepo;
import com.example.quanly_vlxd.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {


    private final CustomerRepo customerRepo;

    private final OutputInvoiceDetailRepo outputInvoiceDetailRepo;

    private final OutputInvoiceRepo outputInvoiceRepo;

    @Autowired
    public CustomerServiceImpl(CustomerRepo customerRepo, OutputInvoiceDetailRepo outputInvoiceDetailRepo, OutputInvoiceRepo outputInvoiceRepo) {
        this.customerRepo = customerRepo;
        this.outputInvoiceDetailRepo = outputInvoiceDetailRepo;
        this.outputInvoiceRepo = outputInvoiceRepo;
    }
    @Override
    public MessageResponse addCustomer(CustomerRequest customerRequest) {
        for (Customer customer : customerRepo.findAll()) {
            if (customer.getPhoneNum().equals(customerRequest.getPhoneNum())) {
                return MessageResponse.builder().message("Customer phone number already exist!!!").build();
            }
        }
        Customer newCus = Customer.builder()
                .name(customerRequest.getName())
                .address(customerRequest.getAddress())
                .phoneNum(customerRequest.getPhoneNum())
                .isActive(true)
                .build();
        customerRepo.save(newCus);
        return MessageResponse.builder().message("Create new customer successfully!!!").build();
    }

    @Override
    public MessageResponse updateCustomer(int id, CustomerRequest customerRequest) {
        Optional<Customer> customer = customerRepo.findById(id);
        if (customer.isEmpty()) {
            return MessageResponse.builder().message("ID: " + id + " is not exist").build();
        }
        List<Customer> customers = customerRepo.findAll();
        customers.removeIf(c -> c.getId() == id);
        for(Customer customer1: customers){
            if(customer1.getPhoneNum().equals(customerRequest.getPhoneNum())){
                return MessageResponse.builder().message("Customer phone number already exist!!!").build();
            }
        }
        Customer cusCur = customer.get();
        cusCur.setName(customerRequest.getName());
        cusCur.setAddress(customerRequest.getAddress());
        cusCur.setPhoneNum(customerRequest.getPhoneNum());
        customerRepo.save(cusCur);
        return MessageResponse.builder().message("Update information successfully!!!").build();
    }

    @Override
    public MessageResponse deleteCustomer(int id) {
        Optional<Customer> customer = customerRepo.findById(id);
        if (customer.isEmpty()) {
            return MessageResponse.builder().message("ID: " + id + " is not exist").build();
        }
        for (OutputInvoice outputInvoice : outputInvoiceRepo.findAll()) {
            if (outputInvoice.getCustomer().getId() == id) {
                for(OutputInvoiceDetail outputInvoiceDetail: outputInvoiceDetailRepo.findAll()){
                    if(outputInvoiceDetail.getOutputInvoice().getId()==outputInvoice.getId()){
                        outputInvoiceDetailRepo.deleteById(outputInvoiceDetail.getId());
                    }
                }
                outputInvoice.setIsActive(false);
                outputInvoiceRepo.save(outputInvoice);
            }
        }
        customer.get().setActive(false);
        customerRepo.save(customer.get());
        return MessageResponse.builder().message("Delete customer successfully!!!").build();
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
    private CusResponse convertToDTO(Customer customer) {
        return CusResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .address(customer.getAddress())
                .phoneNum(customer.getPhoneNum())
                .build();
    }
}
