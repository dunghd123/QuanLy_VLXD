package com.example.quanly_vlxd.entity;

import com.example.quanly_vlxd.enums.InvoiceTypeEnums;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_price_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pph_id")
    private int Id;
    @Column(name = "pph_price")
    private int Price;
    @Column(name = "pph_startdate")
    private String StartDate;
    @Column(name = "pph_enddate")
    private String EndDate;
    @Column(name = "pph_invoice_type")
    @Enumerated(EnumType.STRING)
    private InvoiceTypeEnums InvoiceType;
    @Column(name = "isactive")
    private boolean IsActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pro_id", foreignKey = @ForeignKey(name = "FK_Product_ProductPriceHistory"))
    @JsonIgnoreProperties(value = "productPriceHistories")
    private Product product;
}
