package com.example.quanly_vlxd.service;


import com.example.quanly_vlxd.dto.request.EmpRequest;
import com.example.quanly_vlxd.dto.request.UserRequest;
import com.example.quanly_vlxd.dto.response.EmpResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeService {
    MessageResponse addEmployee(EmpRequest empRequest);
    ResponseEntity<MessageResponse> updateUser(int id, UserRequest userRequest);

    ResponseEntity<MessageResponse> deleteUser(String username);
    Page<EmpResponse> getAllEmployee(int page, int size);
    List<EmpResponse> getAllManager();
}
