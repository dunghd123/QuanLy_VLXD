package com.example.quanly_vlxd.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesDetailResponse {
    private int invoiceID;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date invoiceDate;
    private int productID;
    private String productName;
    private int customerID;
    private String customerName;
    private int employeeID;
    private String employeeName;
    private String warehouseName;
    private String unitMeasure;
    private double unitPrice;
    private double quantitySold;
}
