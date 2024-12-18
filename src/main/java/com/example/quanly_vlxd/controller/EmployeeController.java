package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.request.EmpRequest;
import com.example.quanly_vlxd.dto.response.EmpResponse;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.service.impl.EmployeeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.quanly_vlxd.help.MapErrors.getMapErrors;

@RestController
@RequestMapping("/api/v1/employee/")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeServiceImpl employeeServiceImpl;

    @PostMapping("add-employee")
    public ResponseEntity<MessageResponse> addEmployee(@Valid @RequestBody EmpRequest empRequest){
        return new ResponseEntity<>(employeeServiceImpl.addEmployee(empRequest), HttpStatus.CREATED);
    }
    @PutMapping("update-employee/{id}")
    public ResponseEntity<MessageResponse> updateEmployee(@PathVariable(value = "id") int id,@Valid @RequestBody EmpRequest empRequest){
        return new ResponseEntity<>(employeeServiceImpl.updateEmployee(id, empRequest), HttpStatus.OK);
    }
    @DeleteMapping("delete-employee/{id}")
    public ResponseEntity<MessageResponse> deleteEmployee(@PathVariable(value = "id") int id){
        return new ResponseEntity<>(employeeServiceImpl.deleteEmployee(id), HttpStatus.OK);
    }
    @GetMapping("getAllEmployee")
    public Page<EmpResponse> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return employeeServiceImpl.getAllEmployee(page,size);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // Trả về mã 400 BAD_REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class) // Xử lý ngoại lệ MethodArgumentNotValidException
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getMapErrors(ex);
    }
}
