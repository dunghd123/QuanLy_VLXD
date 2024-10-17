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
    private int Id;
    @Column(name = "name")
    private String Name;
    @Column(name = "address")
    private String Address;
    @Column(name = "phone_num",unique = true)
    private String PhoneNum;
    @Column(name = "isactive")
    private boolean IsActive;

    @OneToMany(mappedBy = "supplier")
    @JsonIgnoreProperties(value = "supplier")
    Set<InputInvoice> inputInvoices;

}
