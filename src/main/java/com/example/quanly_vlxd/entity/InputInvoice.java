package com.example.quanly_vlxd.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "input_invoices")
public class InputInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inp_id")
    private int id;

    @Column(name = "inp_creation_time",nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date creationTime;

    @Column(name = "inp_update_time",nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date updateTime;

    @Column(name = "inp_status")
    private boolean status;

    @Column(name = "inp_total_amount")
    private double totalAmount;

    @Column(name = "isactive")
    private boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sup_id", foreignKey = @ForeignKey(name = "FK_INPUT_INVOICE_SUPPLIER"))
    @JsonIgnoreProperties(value = "inputInvoices")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emp_id", foreignKey = @ForeignKey(name = "FK_INPUT_INVOICE_EMPLOYEE"))
    @JsonIgnoreProperties(value = "inputInvoices")
    private Employee employee;

    @OneToMany(mappedBy = "inputInvoice")
    @JsonIgnoreProperties(value = "inputInvoice")
    Set<InputInvoiceDetail> inputInvoiceDetails;
}
