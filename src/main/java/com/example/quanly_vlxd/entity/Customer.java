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
    private int Id;

    @Column(name = "cus_name",nullable = false)
    private String Name;

    @Column(name = "cus_address",nullable = false)
    private String Address;

    @Column(name = "cus_phonenum",nullable = false,unique = true)
    private String PhoneNum;

    @Column(name = "isactive")
    private boolean IsActive;

    @OneToMany(mappedBy = "customer")
    @JsonIgnoreProperties(value = "customer")
    Set<OutputInvoice> outputInvoices;
}
