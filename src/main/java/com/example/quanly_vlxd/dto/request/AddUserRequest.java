package com.example.quanly_vlxd.dto.request;

import com.example.quanly_vlxd.enums.GenderEnums;
import com.example.quanly_vlxd.enums.RoleEnums;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "0\\d{9}", message = "Phone must start with 0 and have 10 digits")
    private String phone;

    // ManagerId có thể = 0 nếu không có sếp → không dùng @NotNull
    @Min(value = 0, message = "Manager ID must be greater or equal to 0")
    private int managerId;

    @NotBlank(message = "Date of birth is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date of birth must be in format yyyy-MM-dd")
    private String dateOfBirth;

    @NotNull(message = "Gender is required")
    private GenderEnums gender;

    @NotNull(message = "Role is required")
    private RoleEnums role;

}
