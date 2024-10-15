package com.example.quanly_vlxd.controller;

import com.example.quanly_vlxd.dto.request.AddUserRequest;
import com.example.quanly_vlxd.dto.request.ChangePasswordRequest;
import com.example.quanly_vlxd.dto.request.LoginRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.TokenResponse;
import com.example.quanly_vlxd.service.impl.UserSerViceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/user/")
public class UserController {
    @Autowired
    private UserSerViceImpl userSerVice;

    @PostMapping("login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(userSerVice.login(loginRequest), HttpStatus.OK);
    }
    @PostMapping("add-new-user")
    public ResponseEntity<MessageResponse> adduser(@RequestBody AddUserRequest addUserRequest) {
        return new ResponseEntity<>(userSerVice.addUser(addUserRequest), HttpStatus.CREATED);
    }
    @PutMapping("logout")
    public ResponseEntity<MessageResponse> logout(@RequestParam String username){
        return new ResponseEntity<>(userSerVice.logout(username),HttpStatus.OK);
    }
    @PutMapping("change-password")
    public ResponseEntity<MessageResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        return new ResponseEntity<>(userSerVice.changePass(changePasswordRequest),HttpStatus.OK);
    }

}
