package com.example.quanly_vlxd.service.impl;
import com.example.quanly_vlxd.dto.CustomerDTO;
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
    public MessageResponse addCustomer(CustomerDTO customerDTO) {
        for (Customer customer : customerRepo.findAll()) {
            if (customer.getName().equals(customerDTO.getName())) {
                return MessageResponse.builder().message("Customer name already exist!!!").build();
            }
            if (customer.getPhoneNum().equals(customerDTO.getPhoneNum())) {
                return MessageResponse.builder().message("Customer phone number already exist!!!").build();
            }
        }
        Customer newCus = Customer.builder()
                .Name(customerDTO.getName())
                .Address(customerDTO.getAddress())
                .PhoneNum(customerDTO.getPhoneNum())
                .IsActive(true)
                .build();
        customerRepo.save(newCus);
        return MessageResponse.builder().message("Create new customer successfully!!!").build();
    }

    @Override
    public MessageResponse updateCustomer(int id, CustomerDTO customerDTO) {
        Optional<Customer> customer = customerRepo.findById(id);
        if (customer.isEmpty()) {
            return MessageResponse.builder().message("ID: " + id + " is not exist").build();
        }
        List<Customer> customers = customerRepo.findAll();
        customers.removeIf(c -> c.getId() == id);
        for(Customer customer1: customers){
            if(customer1.getName().equals(customerDTO.getName())){
                return MessageResponse.builder().message("Customer name already exist!!!").build();
            }
            if(customer1.getPhoneNum().equals(customerDTO.getPhoneNum())){
                return MessageResponse.builder().message("Customer phone number already exist!!!").build();
            }
        }
        Customer cusCur = customer.get();
        cusCur.setName(customerDTO.getName());
        cusCur.setAddress(customerDTO.getAddress());
        cusCur.setPhoneNum(customerDTO.getPhoneNum());
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
        customer.get().setIsActive(false);
        customerRepo.save(customer.get());
        return MessageResponse.builder().message("Delete customer successfully!!!").build();
    }

    @Override
    public Page<Customer> getAllCustomer(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return customerRepo.getCustomer(pageable);
    }
}
