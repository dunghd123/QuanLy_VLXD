package com.example.quanly_vlxd.service.impl;


import com.example.quanly_vlxd.dto.request.EmpRequest;
import com.example.quanly_vlxd.dto.request.UserRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepo employeeRepo;
    private final UserRepo userRepo;
    private final InputInvoiceRepo inputInvoiceRepo;
    private final OutputInvoiceRepo outputInvoiceRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;

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
    @Transactional
    public ResponseEntity<MessageResponse> updateUser(int id, UserRequest userRequest) {
        Optional<User> optionalUser= userRepo.findById(id);
        if(optionalUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
        }
        User currentUser= optionalUser.get();
        if(userRequest.getPassword()!= null){
            boolean isSamePassword= passwordEncoder.matches(optionalUser.get().getPassword(), userRequest.getPassword());
            if(isSamePassword){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Password is same as old password"));
            }else {
                currentUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            }
        }
        Role role= roleRepo.findByRoleName(userRequest.getRole().name()).orElseThrow(() -> new RuntimeException("Role not found"));
        //save User
        updateUserData(currentUser,userRequest,role,passwordEncoder);
        userRepo.save(currentUser);
        //save employee
        Employee manager= userRequest.getManagerId()!=0 ? employeeRepo.findById(userRequest.getManagerId()).get() : null;
        Employee currentEmployee= employeeRepo.findByUserID(currentUser.getId());

        boolean isPhoneNumberExist= employeeRepo.existsByPhoneNumAndUserIdNot(userRequest.getPhone(),currentUser.getId());
        if(isPhoneNumberExist){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Phone number already exists!"));
        }
        updateEmployeeData(currentEmployee,userRequest,manager);
        employeeRepo.save(currentEmployee);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("User updated successfully"));

    }
    private static void updateUserData(User currentUser, UserRequest userRequest, Role role, PasswordEncoder passwordEncoder ){
        currentUser.setRole(role);
        currentUser.setActive(true);
        currentUser.setStatus(false);
    }
    private static void updateEmployeeData(Employee currentEmployee, UserRequest userRequest, Employee manager){
        currentEmployee.setName(userRequest.getFullName());
        currentEmployee.setAddress(userRequest.getAddress());
        currentEmployee.setDob(Date.valueOf(userRequest.getDateOfBirth()));
        currentEmployee.setGender(userRequest.getGender());
        currentEmployee.setPhoneNum(userRequest.getPhone());
        currentEmployee.setManager(manager);
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
                inputInvoice.setActive(false);
                inputInvoiceRepo.save(inputInvoice);
            }
            for(OutputInvoice outputInvoice: outputInvoices){
                outputInvoice.setActive(false);
                outputInvoiceRepo.save(outputInvoice);
            }
            employee.setActive(false);
            employeeRepo.save(employee);
        }
        optionalUser.get().setActive(false);
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

    @Override
    public EmpResponse getEmpByUsername(String username) {
        Optional<User> optionalUser= userRepo.findByUserName(username);
        if(optionalUser.isEmpty()){
            return null;
        }else {
            Employee employee= employeeRepo.findByUserID(optionalUser.get().getId());
            return convertToDTO(employee);
        }
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

