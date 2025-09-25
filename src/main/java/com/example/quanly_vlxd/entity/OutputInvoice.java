package com.example.quanly_vlxd.entity;

import com.example.quanly_vlxd.enums.InvoiceStatusEnums;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "output_invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutputInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oi_id")
    private int id;

    @Column(name= " oi_code", unique = true)
    @Size(max = 10)
    private String code;

    @Column(name = "oi_creation_time",nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date creationTime;

    @Column(name = "oi_update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date updateTime;

    @Column(name = "oi_ship_address")
    private String shipAddress;

    @Column(name = "oi_status")
    @Enumerated(EnumType.STRING)
    private InvoiceStatusEnums status;

    @Column(name = "oi_total_amount")
    private double totalAmount;

    @Column(name = "isactive")
    private boolean isActive;

    @OneToMany(mappedBy = "outputInvoice")
    @JsonIgnoreProperties(value = "outputInvoice")
    private Set<OutputInvoiceDetail> outputInvoiceDetails;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cus_id", foreignKey = @ForeignKey(name = "FK_OUTPUT_INVOICE_CUSTOMER"))
    @JsonIgnoreProperties(value = "outputInvoices")
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emp_id", foreignKey = @ForeignKey(name = "FK_OUTPUT_INVOICE_EMPLOYEE"))
    @JsonIgnoreProperties(value = "outputInvoices")
    private Employee employee;
}
