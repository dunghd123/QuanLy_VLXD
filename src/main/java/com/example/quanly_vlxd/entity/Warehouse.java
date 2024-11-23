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
@Table(name = "warehouses")
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(name = "name",nullable = false)
    private String Name;

    @Column(name = "location",nullable = false)
    private String Location;

    @Column(name = "isactive")
    private boolean IsActive;

    @OneToMany(mappedBy = "warehouse")
    @JsonIgnoreProperties(value = "warehouse")
    private Set<WareHouse_Product> inventories;

    @OneToMany(mappedBy = "warehouse")
    @JsonIgnoreProperties(value = "warehouse")
    private Set<OutputInvoiceDetail> outputInvoiceDetails;

    @OneToMany(mappedBy = "warehouse")
    @JsonIgnoreProperties(value = "warehouse")
    private Set<InputInvoiceDetail> inputInvoiceDetails;
}
