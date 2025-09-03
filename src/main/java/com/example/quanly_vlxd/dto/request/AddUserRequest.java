package com.example.quanly_vlxd.dto.request;

import com.example.quanly_vlxd.enums.RoleEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserRequest {
    private String username;
    private String password;
    private String fullName;
    private String address;
    private String dateOfBirth;
    private String gender;
    private RoleEnums role;

}
