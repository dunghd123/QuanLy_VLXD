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
    private Set<Inventory> inventories;

    @OneToMany(mappedBy = "warehouse")
    @JsonIgnoreProperties(value = "warehouse")
    private Set<OutputInvoiceDetail> outputInvoiceDetails;
}
