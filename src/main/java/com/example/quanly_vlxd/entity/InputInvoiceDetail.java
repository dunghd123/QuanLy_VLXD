package com.example.quanly_vlxd.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "input_invoice_details")
public class InputInvoiceDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iid_id")
    private int Id;
    @Column(name = "iid_quantity")
    private int Quantity;
    @Column(name = "iid_unit_price")
    private double UnitPrice;
    @Column(name = "iid_amount")
    private double Amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inp_id", foreignKey = @ForeignKey(name = "FK_INPUT_INVOICE_DETAIL_INVOICE"))
    @JsonIgnoreProperties(value = "inputInvoiceDetails")
    private InputInvoice inputInvoice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pro_id", foreignKey = @ForeignKey(name = "FK_INPUT_INVOICE_DETAIL_PRODUCT"))
    @JsonIgnoreProperties(value = "inputInvoiceDetails")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wh_id", foreignKey = @ForeignKey(name = "FK_INPUT_INVOICE_DETAIL_WAREHOUSE"))
    @JsonIgnoreProperties(value = "inputInvoiceDetails")
    private Warehouse warehouse;
}
