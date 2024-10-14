package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
    @Query(value = "select * from users u where u.username like ?1",nativeQuery = true)
    Optional<User> findByUserName(String username);
}
