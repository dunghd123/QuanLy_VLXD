package com.example.quanly_vlxd.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesInputResponse {
    private int invoiceID;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date invoiceDate;
    private int productID;
    private String productName;
    private int supplierID;
    private String supplierName;
    private int employeeID;
    private String employeeName;
    private String warehouseName;
    private String unitMeasure;
    private double unitPrice;
    private double quantityBuy;
}
