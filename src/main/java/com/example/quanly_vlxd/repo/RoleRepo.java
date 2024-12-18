package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role,Integer> {
    @Query(value = "select * from roles r where r.role_name like ?1",nativeQuery = true)
    Optional<Role> findByRoleName(String rolename);
}
