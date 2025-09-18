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
@Table(name = "suppliers")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sup_id")
    private int id;
    @Column(name = "sup_name")
    private String name;
    @Column(name = "sup_address")
    private String address;
    @Column(name = "sup_phonenum",unique = true)
    private String phoneNum;
    @Column(name = "isactive")
    private boolean isActive;

    @OneToMany(mappedBy = "supplier")
    @JsonIgnoreProperties(value = "supplier")
    Set<InputInvoice> inputInvoices;

}
