package com.example.quanly_vlxd.service.impl;


import com.example.quanly_vlxd.dto.request.EmpRequest;
import com.example.quanly_vlxd.dto.response.EmpResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.entity.*;
import com.example.quanly_vlxd.repo.*;
import com.example.quanly_vlxd.service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepo employeeRepo;
    private final UserRepo userRepo;
    private final InputInvoiceRepo inputInvoiceRepo;
    private final InputInvoiceDetailRepo inputInvoiceDetailRepo;
    private final OutputInvoiceRepo outputInvoiceRepo;
    private final OutputInvoiceDetailRepo OutputInvoiceDetailRepo;

    @Override
    public MessageResponse addEmployee(EmpRequest empRequest) {
        Optional<User> findbyUsername= userRepo.findByUserName(empRequest.getUsername());
        if(findbyUsername.isEmpty()){
            return MessageResponse.builder().message("User does not exist!!").build();
        }
        if(findbyUsername.get().getRole().getId() != 1){
            return MessageResponse.builder().message("User is not employee account!!").build();
        }
        if(!findbyUsername.get().isActive()){
            return MessageResponse.builder().message("User is not active!!").build();
        }
        if(!employeeRepo.findAll().isEmpty()){
            for(Employee employee: employeeRepo.findAll()){
                if(employee.getPhoneNum().equals(empRequest.getPhoneNum())){
                    return MessageResponse.builder().message("Phone number already exist!!!").build();
                }
                if(findbyUsername.get().getUserName().equals(employee.getUser().getUserName())){
                    return MessageResponse.builder().message("User already in use!!!").build();
                }
            }
        }
        Employee employee = Employee.builder()
                .name(empRequest.getName())
                .dob(empRequest.getDob())
                .gender(empRequest.getGender())
                .phoneNum(empRequest.getPhoneNum())
                .address(empRequest.getAddress())
                .description(empRequest.getDescription())
                .user(findbyUsername.get())
                .isActive(true)
                .build();
        employeeRepo.save(employee);
        return MessageResponse.builder().message("Add employee successfully!!").build();

    }
    @Override
    public MessageResponse updateEmployee(int id, EmpRequest empRequest) {
        Optional<Employee> employee = employeeRepo.findById(id);
        Optional<User> findbyUsername= userRepo.findByUserName(empRequest.getUsername());
        if(employee.isEmpty()){
            return MessageResponse.builder().message("Employee does not exist!!").build();
        }
        if(findbyUsername.isEmpty()){
            return MessageResponse.builder().message("User does not exist!!").build();
        }
        List<Employee> employees = employeeRepo.findAll();
        employees.removeIf(e -> e.getId() == id);
        for(Employee e: employees){
            if(findbyUsername.get().getUserName().equals(e.getUser().getUserName())){
                return MessageResponse.builder().message("User already in use!!!").build();
            }
            if(e.getPhoneNum().equals(empRequest.getPhoneNum())){
                return MessageResponse.builder().message("Phone number already exist!!!").build();
            }
        }
        Employee employeeCur = employee.get();
        employeeCur.setName(empRequest.getName());
        employeeCur.setDob(empRequest.getDob());
        employeeCur.setGender(empRequest.getGender());
        employeeCur.setPhoneNum(empRequest.getPhoneNum());
        employeeCur.setAddress(empRequest.getAddress());
        employeeCur.setDescription(empRequest.getDescription());
        employeeCur.setUser(findbyUsername.get());
        employeeRepo.save(employeeCur);
        return MessageResponse.builder().message("Update employee successfully!!").build();
    }
    @Override
    @Transactional
    public ResponseEntity<MessageResponse> deleteUser(String username) {
        Optional<User> optionalUser= userRepo.findByUserName(username);
        if(optionalUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
        }else {
            Employee employee= employeeRepo.findByUserID(optionalUser.get().getId());
            List<InputInvoice> inputInvoices = inputInvoiceRepo.findByEmployeeId(employee.getId());
            List<OutputInvoice> outputInvoices = outputInvoiceRepo.findByEmployeeId(employee.getId());
            for(InputInvoice inputInvoice: inputInvoices){
                inputInvoice.setIsActive(false);
                inputInvoiceRepo.save(inputInvoice);
            }
            for(OutputInvoice outputInvoice: outputInvoices){
                outputInvoice.setIsActive(false);
                outputInvoiceRepo.save(outputInvoice);
            }
            employee.setActive(false);
            employeeRepo.save(employee);
        }
        optionalUser.get().setStatus(false);
        userRepo.save(optionalUser.get());
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("User deleted successfully"));
    }

    @Override
    public Page<EmpResponse> getAllEmployee(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepo.getAll(pageable).map(this::convertToDTO);
    }

    @Override
    public List<EmpResponse> getAllManager() {
        return employeeRepo.getAllManager().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private EmpResponse convertToDTO(Employee employee) {
        return EmpResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .gender(employee.getGender())
                .dob(employee.getDob())
                .address(employee.getAddress())
                .phoneNum(employee.getPhoneNum())
                .description(employee.getDescription())
                .isActive(employee.isActive())
                .build();
    }
}

