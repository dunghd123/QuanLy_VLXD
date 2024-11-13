package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.CustomerDTO;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.entity.Customer;
import com.example.quanly_vlxd.service.impl.CustomerServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.quanly_vlxd.help.MapErrors.getMapErrors;

@RestController
@RequestMapping("/api/v1/customer/")
public class CustomerController {
    @Autowired
    private CustomerServiceImpl customerServiceImpl;

    @PostMapping("add-new-customer")
    public ResponseEntity<MessageResponse> addNewCustomer(@Valid @RequestBody CustomerDTO customerDTO){
        return new ResponseEntity<>(customerServiceImpl.addCustomer(customerDTO), HttpStatus.CREATED);
    }
    @PutMapping("update-customer/{id}")
    public ResponseEntity<MessageResponse> updateCustomer(@PathVariable(value = "id") int id,@Valid @RequestBody CustomerDTO customerDTO){
        return new ResponseEntity<>(customerServiceImpl.updateCustomer(id, customerDTO), HttpStatus.OK);
    }
    @DeleteMapping("delete-customer/{id}")
    public ResponseEntity<MessageResponse> deleteCustomer(@PathVariable(value = "id") int id){
        return new ResponseEntity<>(customerServiceImpl.deleteCustomer(id), HttpStatus.OK);
    }
    @GetMapping("getAllCustomer")
    public Page<Customer> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return customerServiceImpl.getAllCustomer(page,size);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Trả về mã 400 BAD_REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class) // Xử lý ngoại lệ MethodArgumentNotValidException
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getMapErrors(ex);
    }

}
