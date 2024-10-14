package com.example.quanly_vlxd.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InputInvoiceDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(name = "quantity")
    private int Quantity;

    @Column(name = "unit_price")

    private double UnitPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inp_id", foreignKey = @ForeignKey(name = "FK_INPUT_INVOICE_DETAIL_INVOICE"))
    @JsonIgnoreProperties(value = "inputInvoiceDetails")
    private InputInvoice inputInvoice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pro_id", foreignKey = @ForeignKey(name = "FK_INPUT_INVOICE_DETAIL_PRODUCT"))
    @JsonIgnoreProperties(value = "inputInvoiceDetails")
    private Product product;
}
