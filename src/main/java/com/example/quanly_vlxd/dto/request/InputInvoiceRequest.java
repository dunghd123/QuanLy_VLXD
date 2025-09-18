package com.example.quanly_vlxd.dto.request;

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

public class InputInvoiceRequest {

    @Min(value = 1, message = "Supplier ID must be Integer and min value equal 1")
    private int  supID;

    @Min(value = 1, message = "Employee ID must be Integer and min value equal 1")
    private int empID;

    @NotNull(message = "Creation time can not be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Date creationTime;

    @NotEmpty(message = "Input Invoice details list can not be empty")
    private List<InputInvoiceDetailRequest> listInvoiceDetails;
}
