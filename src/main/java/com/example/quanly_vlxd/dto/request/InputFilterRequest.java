package com.example.quanly_vlxd.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputFilterRequest {
    private String supNameFilter;
    private Boolean statusFilter;
    private int pageFilter;
    private int sizeFilter;
}
