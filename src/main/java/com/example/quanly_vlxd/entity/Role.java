package com.example.quanly_vlxd.entity;

import com.example.quanly_vlxd.enums.RoleEnums;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
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
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int Id;
    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleEnums RoleName;

    @OneToMany(mappedBy = "role")
    @JsonIgnoreProperties(value = "role")
    Set<User> users;
}
