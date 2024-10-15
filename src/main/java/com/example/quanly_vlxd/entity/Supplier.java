package com.example.quanly_vlxd.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {
    @Id
    @Column(name = "id",nullable = false,unique = true)
    private String Id;
    @Column(name = "name",nullable = false)
    private String Name;
    @Column(name = "address",nullable = false)
    private String Address;
    @Column(name = "phone_num",nullable = false,unique = true)
    private String PhoneNum;
    @Column(name = "isactive")
    private boolean IsActive;

    @OneToMany(mappedBy = "supplier")
    @JsonIgnoreProperties(value = "supplier")
    Set<InputInvoice> inputInvoices;

}
