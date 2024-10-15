package com.example.quanly_vlxd.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(name = "name",nullable = false)
    private String Name;

    @Column(name = "unit_measure",nullable = false)
    private String UnitMeasure;

    @Column(name = "origin",nullable = false)
    private String Origin;

    @Column(name = "isactive")
    private boolean IsActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cate_id", foreignKey = @ForeignKey(name = "FK_PRODUCT_CATEGORY"))
    @JsonIgnoreProperties(value = "products")
    private Category category;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties(value = "product")
    Set<Inventory> inventories;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties(value = "product")
    Set<InputInvoiceDetail> inputInvoiceDetails;
}
