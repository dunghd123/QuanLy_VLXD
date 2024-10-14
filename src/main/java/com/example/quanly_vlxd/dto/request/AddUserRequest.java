package com.example.quanly_vlxd.dto.request;

import com.example.quanly_vlxd.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserRequest {
    private String username;
    private String password;
    private Role role;
}
