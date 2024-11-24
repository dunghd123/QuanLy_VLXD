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
    @Column(name = "oid_id")
    private int id;
    @Column(name = "pro_id",nullable = false)
    private int Pro_Id;
    @Column(name = "oid_quantity",nullable = false)
    private double quantity;
    @Column(name = "oid_unit_price",nullable = false)
    private double unitPrice;
    @Column (name = "oid_amount",nullable = false)
    private double Amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "oi_id", foreignKey = @ForeignKey(name = "FK_OUTPUT_INVOICE_DETAIL_OUTPUT_INVOICE"))
    @JsonIgnoreProperties(value = "outputInvoice")
    private OutputInvoice outputInvoice;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wh_id", foreignKey = @ForeignKey(name = "FK_OUTPUT_INVOICE_DETAIL_WAREHOUSE"))
    @JsonIgnoreProperties(value = "outputInvoiceDetails")
    private Warehouse warehouse;
}
