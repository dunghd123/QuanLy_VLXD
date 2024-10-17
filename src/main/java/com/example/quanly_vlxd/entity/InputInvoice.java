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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "input_invoices")
public class InputInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(name = "creation_time",nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date CreationTime;

    @Column(name = "update_time",nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date UpdateTime;

    @Column(name = "isactive")
    private boolean IsActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", foreignKey = @ForeignKey(name = "FK_INPUT_INVOICE_STATUS"))
    @JsonIgnoreProperties(value = "inputInvoices")
    private StatusInvoice status;

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
