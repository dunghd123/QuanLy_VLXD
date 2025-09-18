package com.example.quanly_vlxd.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
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
    private int Id;

    @Column(name = "oi_creation_time",nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date CreationTime;

    @Column(name = "oi_update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date UpdateTime;

    @Column(name = "oi_ship_address",nullable = false)
    private String ShipAddress;

    @Column(name = "oi_status")
    private boolean Status;

    @Column(name = "oi_total_amount")
    private double TotalAmount;

    @Column(name = "isactive")
    private boolean IsActive;

    @OneToMany(mappedBy = "outputInvoice", cascade = CascadeType.ALL)
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
