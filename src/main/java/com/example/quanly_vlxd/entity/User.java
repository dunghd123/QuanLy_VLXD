package com.example.quanly_vlxd.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    @Column(name = "username",nullable = false,unique = true)
    private String UserName;
    @Column(name = "password",nullable = false)
    private String Password;
    @Column(name = "status",nullable = false)
    private boolean Status;
    @Column(name = "isactive")
    private boolean IsActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleid",foreignKey = @ForeignKey(name = "FK_USER_ROLE"))
    @JsonIgnoreProperties(value = "users")
    private Role role;

    @OneToOne(mappedBy = "user")
    @JsonIgnoreProperties(value = "user")
    private Employee employee;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = "user")
    Set<RefreshToken> refreshTokens;
}
