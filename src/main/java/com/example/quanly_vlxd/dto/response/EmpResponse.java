package com.example.quanly_vlxd.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmpResponse {
    private int id;
    private String name;
    private String gender;
    @JsonFormat(pattern = "dd-MM-yyyy",shape = JsonFormat.Shape.STRING,timezone = "GMT+7")
    private Date dob;
    private String address;
    private String phoneNum;
    private String description;
    private boolean isActive;
}
