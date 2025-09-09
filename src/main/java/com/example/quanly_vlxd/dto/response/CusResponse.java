package com.example.quanly_vlxd.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CusResponse {
    private int id;
    private String name;
    private String address;
    private String phoneNum;
}
