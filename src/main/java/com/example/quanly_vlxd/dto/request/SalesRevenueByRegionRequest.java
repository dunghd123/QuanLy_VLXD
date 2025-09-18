package com.example.quanly_vlxd.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesRevenueByRegionRequest{
    @Size(max =2, message = "List only contains less than or equal 2")
    private List<String> region;
}