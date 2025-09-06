package com.example.quanly_vlxd.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private int id;
    private String userName;
    private String fullName;
    private String role;
    private String phone;
    private boolean status;
    private String gender;
    private String address;
    private String dateOfBirth;
    private Integer managerId;
}
