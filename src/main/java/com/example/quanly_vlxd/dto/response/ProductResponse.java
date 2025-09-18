package com.example.quanly_vlxd.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private int id;
    private String name;
    private String unitMeasure;
    private String description;
    private String cateName;
}
