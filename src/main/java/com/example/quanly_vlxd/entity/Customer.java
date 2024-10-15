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
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(name = "name",nullable = false)
    private String Name;

    @Column(name = "address",nullable = false)
    private String Address;

    @Column(name = "phone_num",nullable = false,unique = true)
    private String PhoneNum;

    @Column(name = "isactive")
    private boolean IsActive;

    @OneToMany(mappedBy = "customer")
    @JsonIgnoreProperties(value = "customer")
    Set<OutputInvoice> outputInvoices;
}
