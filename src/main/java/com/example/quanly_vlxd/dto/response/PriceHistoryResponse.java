package com.example.quanly_vlxd.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceHistoryResponse {
    private int id;
    private String productName;
    private String invoiceType;
    private double price;
    @JsonFormat(pattern = "dd-MM-yyyy",shape = JsonFormat.Shape.STRING,timezone = "GMT+7")
    private Date startDate;
    @JsonFormat(pattern = "dd-MM-yyyy",shape = JsonFormat.Shape.STRING,timezone = "GMT+7")
    private Date endDate;
    private boolean isActive;
}
