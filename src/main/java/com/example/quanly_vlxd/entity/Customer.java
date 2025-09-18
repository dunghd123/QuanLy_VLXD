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
@Table(name = "customers")
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cus_id")
    private int id;

    @Column(name = "cus_name",nullable = false)
    private String name;

    @Column(name = "cus_address",nullable = false)
    private String address;

    @Column(name = "cus_phonenum",nullable = false,unique = true)
    private String phoneNum;

    @Column(name = "isactive")
    private boolean isActive;

    @OneToMany(mappedBy = "customer")
    @JsonIgnoreProperties(value = "customer")
    Set<OutputInvoice> outputInvoices;
}
