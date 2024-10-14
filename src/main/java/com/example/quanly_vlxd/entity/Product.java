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
    @Column(name = "id")
    private String Id;

    @Column(name = "name")
    private String Name;

    @Column(name = "unit_measure")
    private String UnitMeasure;

    @Column(name = "origin")
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
