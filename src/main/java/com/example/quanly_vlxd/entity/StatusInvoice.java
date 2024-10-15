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
public class StatusInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(name = "name",nullable = false)
    private String Name;

    @OneToMany(mappedBy = "status")
    @JsonIgnoreProperties(value = "status")
    Set<OutputInvoice> outputInvoices;

    @OneToMany(mappedBy = "status")
    @JsonIgnoreProperties(value = "status")
    Set<InputInvoice> inputInvoices;
}
