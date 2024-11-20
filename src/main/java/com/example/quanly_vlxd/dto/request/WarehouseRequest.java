package com.example.quanly_vlxd.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseRequest {
    @NotBlank(message = "Warehouse name is required")
    private String name;
    @NotBlank(message = "Location is required")
    private String location;
}
