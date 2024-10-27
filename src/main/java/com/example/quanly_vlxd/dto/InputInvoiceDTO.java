package com.example.quanly_vlxd.dto;

import com.example.quanly_vlxd.entity.InputInvoiceDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class InputInvoiceDTO {
    @NotNull(message = "Creation time can not be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Date creationTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Date updateTime;
    @Min(value = 1, message = "Status ID must be Integer and min value equal 1")
    private int staID;
    @Min(value = 1, message = "Supplier ID must be Integer and min value equal 1")
    private int  supID;
    @Min(value = 1, message = "Employee ID must be Integer and min value equal 1")
    private int empID;
    @NotEmpty(message = "Input Invoice details list can not be empty")
    private List<InputInvoiceDetailDTO> list;
}
