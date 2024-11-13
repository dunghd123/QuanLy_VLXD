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
    private int Id;
    @Column(name = "sup_name")
    private String Name;
    @Column(name = "sup_address")
    private String Address;
    @Column(name = "sup_phonenum",unique = true)
    private String PhoneNum;
    @Column(name = "isactive")
    private boolean IsActive;

    @OneToMany(mappedBy = "supplier")
    @JsonIgnoreProperties(value = "supplier")
    Set<InputInvoice> inputInvoices;

}
