package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.RefreshToken;
import com.example.quanly_vlxd.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken,Integer> {
    @Transactional
    void deleteByUser(User user);
    Optional<RefreshToken> findByToken(String token);
}
