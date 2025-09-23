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
public class OutputInvoiceResponse {
    private int id;
    private String cusName;
    private String empName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Date creationTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Date updateTime;
    private String shipAddress;
    private String status;
    private Set<OutputInvoiceDetailResponse> listOutputInvoiceDetails;
    private double totalAmount;
}
