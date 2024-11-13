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
@Table(name = "categories")
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(name = "name",nullable = false)
    private String Name;

    @Column(name = "description")
    private String Description;

    @Column(name = "isactive")
    private boolean IsActive;

    @OneToMany(mappedBy = "category")
    @JsonIgnoreProperties(value = "category")
    private Set<Product> products;

}
