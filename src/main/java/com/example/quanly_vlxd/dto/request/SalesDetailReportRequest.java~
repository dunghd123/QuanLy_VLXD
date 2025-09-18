package com.example.quanly_vlxd.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesDetailReportRequest {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date endDate;
    private List<Integer> productIds;
    private List<Integer> customerIds;
    private List<Integer> employeeIds;
}