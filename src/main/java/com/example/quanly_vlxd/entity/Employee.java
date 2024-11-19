package com.example.quanly_vlxd.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private int Id;

    @Column(name = "emp_name",nullable = false)
    private String Name;

    @Column(name = "emp_gender",nullable = false)
    private String Gender;

    @Column(name = "emp_dob",nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy",shape = JsonFormat.Shape.STRING,timezone = "GMT+7")
    private Date Dob;

    @Column(name = "emp_address",nullable = false)
    private String Address;

    @Column(name = "emp_phonenum",nullable = false,unique = true)
    private String PhoneNum;

    @Column(name = "emp_description")
    private String Description;

    @Column(name = "isactive")
    private boolean IsActive;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "FK_USER"))
    private User user;

    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties(value = "employee")
    Set<InputInvoice> inputInvoices;

    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties(value = "employee")
    Set<OutputInvoice> outputInvoices;
}
