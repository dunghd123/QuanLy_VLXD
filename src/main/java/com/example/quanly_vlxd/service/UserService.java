package com.example.quanly_vlxd.service;

import com.example.quanly_vlxd.dto.request.AddUserRequest;
import com.example.quanly_vlxd.dto.request.ChangePasswordRequest;
import com.example.quanly_vlxd.dto.request.LoginRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.TokenResponse;


import java.io.UnsupportedEncodingException;

public interface UserService {
    TokenResponse login(LoginRequest loginRequest);
    MessageResponse addUser(AddUserRequest addUserRequest) throws UnsupportedEncodingException;
    MessageResponse logout(String username);

    MessageResponse changePass(ChangePasswordRequest changePasswordRequest);
}
