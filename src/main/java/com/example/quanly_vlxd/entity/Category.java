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
    @Column(name = "cate_id")
    private int id;

    @Column(name = "cate_name",nullable = false,unique = true)
    private String name;

    @Column(name = "cate_description")
    private String description;

    @Column(name = "isactive")
    private boolean isActive;

    @OneToMany(mappedBy = "category")
    @JsonIgnoreProperties(value = "category")
    private Set<Product> products;

}
