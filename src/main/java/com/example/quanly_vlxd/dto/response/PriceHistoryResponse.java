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
    private String invoiceType;
    private String productName;
    private double price;
    private String unitMeasure;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",shape = JsonFormat.Shape.STRING,timezone = "GMT+7")
    private Date startDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",shape = JsonFormat.Shape.STRING,timezone = "GMT+7")
    private Date endDate;
    private boolean isActive;
}
