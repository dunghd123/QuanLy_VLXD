package com.example.quanly_vlxd.entity;

import com.example.quanly_vlxd.enums.InvoiceTypeEnums;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "product_price_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pph_id")
    private int id;
    @Column(name = "pph_price")
    private double price;
    @Column(name = "pph_startdate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date startDate;
    @Column(name = "pph_enddate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date endDate;
    @Column(name = "pph_invoice_type")
    @Enumerated(EnumType.STRING)
    private InvoiceTypeEnums invoiceType;
    @Column(name = "isactive")
    private boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pro_id", foreignKey = @ForeignKey(name = "FK_Product_ProductPriceHistory"))
    @JsonIgnoreProperties(value = "productPriceHistories")
    private Product product;
}
