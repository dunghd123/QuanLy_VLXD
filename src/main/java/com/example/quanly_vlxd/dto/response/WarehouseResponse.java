package com.example.quanly_vlxd.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseResponse {
    private int id;
    private String name;
    private String location;
    private boolean isActive;
}
