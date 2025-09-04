package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.request.AddUserRequest;
import com.example.quanly_vlxd.dto.request.ChangePasswordRequest;
import com.example.quanly_vlxd.dto.request.LoginRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.TokenResponse;
import com.example.quanly_vlxd.dto.response.UserResponse;
import com.example.quanly_vlxd.service.impl.UserSerViceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.quanly_vlxd.help.MapErrors.getMapErrors;


@RestController
@RequestMapping("/api/v1/user/")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserSerViceImpl userSerVice;

    @PostMapping("login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(userSerVice.login(loginRequest), HttpStatus.OK);
    }
    @PostMapping("refresh-token")
    public ResponseEntity<TokenResponse> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        return ResponseEntity.ok(userSerVice.refreshToken(refreshToken));
    }
    @PostMapping("add-new-user")
    public ResponseEntity<MessageResponse> adduser(@Valid @RequestBody AddUserRequest addUserRequest) {
        return userSerVice.addUser(addUserRequest);
    }
    @PutMapping("logout")
    public ResponseEntity<MessageResponse> logout(@RequestParam String username){
        return new ResponseEntity<>(userSerVice.logout(username),HttpStatus.OK);
    }
    @PutMapping("change-password")
    public ResponseEntity<MessageResponse> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        return new ResponseEntity<>(userSerVice.changePass(changePasswordRequest),HttpStatus.OK);
    }
    @GetMapping("getAllEmployee")
    public ResponseEntity<List<UserResponse>> getAllEmployee(){
        return new ResponseEntity<>(userSerVice.getAllUserActive(),HttpStatus.OK);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Trả về mã 400 BAD_REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class) // Xử lý ngoại lệ MethodArgumentNotValidException
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getMapErrors(ex);
    }

}
