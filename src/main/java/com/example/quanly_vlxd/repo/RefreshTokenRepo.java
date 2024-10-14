package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken,Integer> {
}
