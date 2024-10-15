package com.example.quanly_vlxd.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(name = "name",nullable = false)
    private String Name;

    @Column(name = "gender",nullable = false)
    private String Gender;

    @Column(name = "dob",nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy",shape = JsonFormat.Shape.STRING,timezone = "GMT+7")
    private Date Dob;

    @Column(name = "address",nullable = false)
    private String Address;

    @Column(name = "phone_num",nullable = false,unique = true)
    private String PhoneNum;

    @Column(name = "description")
    private String Description;

    @Column(name = "isactive")
    private boolean IsActive;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_EMPLOYEE_USER"))
    @JsonIgnoreProperties(value = "employee")
    private User user;

    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties(value = "employee")
    Set<InputInvoice> inputInvoices;

    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties(value = "employee")
    Set<OutputInvoice> outputInvoices;
}
