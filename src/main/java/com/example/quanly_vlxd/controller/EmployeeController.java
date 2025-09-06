package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.request.EmpRequest;
import com.example.quanly_vlxd.dto.request.UserRequest;
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

import java.util.List;
import java.util.Map;

import static com.example.quanly_vlxd.help.MapErrors.getMapErrors;

@RestController
@RequestMapping("/api/v1/employee/")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmployeeController {
    private final EmployeeServiceImpl employeeServiceImpl;

    @PostMapping("add-employee")
    public ResponseEntity<MessageResponse> addEmployee(@Valid @RequestBody EmpRequest empRequest){
        return new ResponseEntity<>(employeeServiceImpl.addEmployee(empRequest), HttpStatus.CREATED);
    }
    @PutMapping("update-user/{id}")
    public ResponseEntity<MessageResponse> updateEmployee(@PathVariable(value = "id") int id,@Valid @RequestBody UserRequest userRequest){
        return employeeServiceImpl.updateUser(id, userRequest);
    }
    @DeleteMapping("delete-user")
    public ResponseEntity<MessageResponse> deleteUser(@RequestParam String username){
        return employeeServiceImpl.deleteUser(username);
    }
    @GetMapping("getAllEmployee")
    public Page<EmpResponse> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return employeeServiceImpl.getAllEmployee(page,size);
    }

    @GetMapping("getAllManager")
    public List<EmpResponse> getAllManager(){
        return employeeServiceImpl.getAllManager();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // Trả về mã 400 BAD_REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class) // Xử lý ngoại lệ MethodArgumentNotValidException
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getMapErrors(ex);
    }
}
