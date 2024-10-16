package com.example.quanly_vlxd.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "output_invoice_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutputInvoiceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "out_id", foreignKey = @ForeignKey(name = "FK_OUTPUT_INVOICE_DETAIL_OUTPUT_INVOICE"))
    @JsonIgnoreProperties(value = "outputInvoice")
    private OutputInvoice outputInvoice;

    @Column(name = "pro_id",nullable = false)
    private int Pro_Id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wh_id", foreignKey = @ForeignKey(name = "FK_OUTPUT_INVOICE_DETAIL_WAREHOUSE"))
    @JsonIgnoreProperties(value = "outputInvoiceDetails")
    private Warehouse warehouse;

    @Column(name = "quantity",nullable = false)
    private int quantity;

    @Column(name = "unit_price",nullable = false)
    private double unitPrice;
}
