package com.example.quanly_vlxd.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Supplier {
    @Id
    @Column(name = "id")
    private String Id;
    @Column(name = "name")
    private String Name;
    @Column(name = "address")
    private String Address;
    @Column(name = "phone_num")
    private String PhoneNum;
    @Column(name = "isactive")
    private boolean IsActive;

    @OneToMany(mappedBy = "supplier")
    @JsonIgnoreProperties(value = "supplier")
    Set<InputInvoice> inputInvoices;

}
