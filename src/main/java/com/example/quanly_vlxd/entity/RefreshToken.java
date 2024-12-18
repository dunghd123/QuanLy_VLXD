package com.example.quanly_vlxd.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refreshtokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ref_id")
    private int Id;
    @Column(name = "token")
    private String token;
    @Column(name = "expiredtime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ExpiredTime;
    //FK User
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(name = "FK_RefreshToken_User"))
    @JsonIgnoreProperties(value = "refreshTokens")
    private User user;


}
