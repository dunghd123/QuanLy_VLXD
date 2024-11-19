package com.example.quanly_vlxd.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int ID;
    @Column(name = "username",nullable = false,unique = true)
    private String UserName;
    @Column(name = "password",nullable = false)
    private String Password;
    @Column(name = "user_status",nullable = false)
    private boolean Status;
    @Column(name = "isactive")
    private boolean IsActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id",foreignKey = @ForeignKey(name = "FK_USER_ROLE"))
    @JsonIgnoreProperties(value = "users")
    private Role role;

    @OneToOne(mappedBy = "user")
    private Employee employee;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = "user")
    Set<RefreshToken> refreshTokens;
}
