package com.example.quanly_vlxd.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String userName;
    private String fullName;
    private String role;
    private String phone;
    private boolean status;
    private String gender;
}
