package com.example.quanly_vlxd.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pro_id")
    private int Id;

    @Column(name = "pro_name",nullable = false,unique = true)
    private String Name;

    @Column(name = "pro_unit_measure",nullable = false)
    private String UnitMeasure;

    @Column(name = "pro_description")
    private String Description;  ;

    @Column(name = "isactive")
    private boolean IsActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cate_id", foreignKey = @ForeignKey(name = "FK_PRODUCT_CATEGORY"))
    @JsonIgnoreProperties(value = "products")
    private Category category;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties(value = "product")
    Set<WareHouse_Product> inventories;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties(value = "product")
    Set<InputInvoiceDetail> inputInvoiceDetails;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties(value = "product")
    Set<ProductPriceHistory> productPriceHistories;
}
