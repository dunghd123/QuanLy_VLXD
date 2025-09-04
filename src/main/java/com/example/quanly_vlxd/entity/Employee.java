package com.example.quanly_vlxd.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.util.List;
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
    private int id;

    @Column(name = "emp_name",nullable = false)
    private String name;

    @Column(name = "emp_gender",nullable = false)
    private String gender;

    @Column(name = "emp_dob",nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    @Column(name = "emp_address",nullable = false)
    private String address;

    @Column(name = "emp_phonenum",nullable = false,unique = true)
    private String phoneNum;

    @Column(name = "emp_description")
    private String description;

    @Column(name = "isactive")
    private boolean isActive;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "FK_USER"))
    private User user;

    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties(value = "employee")
    Set<InputInvoice> inputInvoices;

    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties(value = "employee")
    Set<OutputInvoice> outputInvoices;

    //self-join
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private List<Employee> subordinates;
}
