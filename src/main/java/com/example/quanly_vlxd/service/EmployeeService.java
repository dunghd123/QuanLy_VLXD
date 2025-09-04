package com.example.quanly_vlxd.service;


import com.example.quanly_vlxd.dto.request.EmpRequest;
import com.example.quanly_vlxd.dto.response.EmpResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {
    MessageResponse addEmployee(EmpRequest empRequest);
    MessageResponse updateEmployee(int id, EmpRequest empRequest);

    MessageResponse deleteEmployee(int id);
    Page<EmpResponse> getAllEmployee(int page, int size);
    List<EmpResponse> getAllManager();
}
