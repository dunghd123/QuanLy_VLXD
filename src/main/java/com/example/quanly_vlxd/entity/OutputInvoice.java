package com.example.quanly_vlxd.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "output_invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OutputInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cus_id", foreignKey = @ForeignKey(name = "FK_OUTPUT_INVOICE_CUSTOMER"))
    @JsonIgnoreProperties(value = "outputInvoices")
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emp_id", foreignKey = @ForeignKey(name = "FK_OUTPUT_INVOICE_EMPLOYEE"))
    @JsonIgnoreProperties(value = "outputInvoices")
    private Employee employee;

    @Column(name = "creation_time",nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date CreationTime;

    @Column(name = "update_time",nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date UpdateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", foreignKey = @ForeignKey(name = "FK_OUTPUT_INVOICE_STATUS"))
    @JsonIgnoreProperties(value = "outputInvoices")
    private StatusInvoice status;

    @Column(name = "isactive")
    private boolean IsActive;

    @OneToMany(mappedBy = "outputInvoice", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "outputInvoice")
    private Set<OutputInvoiceDetail> outputInvoiceDetails;
}
