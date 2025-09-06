package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.request.UserRequest;
import com.example.quanly_vlxd.dto.request.ChangePasswordRequest;
import com.example.quanly_vlxd.dto.request.LoginRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.TokenResponse;
import com.example.quanly_vlxd.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;


import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {
    TokenResponse login(LoginRequest loginRequest);
    TokenResponse refreshToken(String refreshToken);
    ResponseEntity<MessageResponse> addUser(UserRequest userRequest) throws UnsupportedEncodingException;
    MessageResponse logout(String username);

    MessageResponse changePass(ChangePasswordRequest changePasswordRequest);
    List<UserResponse> getAllUserActive();
}
