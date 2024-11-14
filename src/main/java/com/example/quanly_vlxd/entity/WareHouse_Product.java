package com.example.quanly_vlxd.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "warehouse_products")
public class WareHouse_Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wp_id")
    private int Id;
    @Column(name = "wp_quantity",nullable = false)
    private int Quantity;
    @Column(name = "wp_last_updated",nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date LastUpdated;

    @Column(name = "isactive")
    private boolean IsActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wh_id", foreignKey = @ForeignKey(name = "FK_INVENTORY_WAREHOUSE"))
    @JsonIgnoreProperties(value = "warehouse")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pro_id", foreignKey = @ForeignKey(name = "FK_INVENTORY_PRODUCT"))
    @JsonIgnoreProperties(value = "inventories")
    private Product product;
}