package com.example.quanly_vlxd.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InputInvoiceResponse {
    private int id;
    private String supName;
    private String empName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Date creationTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Date updateTime;
    private String status;
    private Set<InputInvoiceDetailResponse> listInvoiceDetails;
    private double totalAmount;
}
