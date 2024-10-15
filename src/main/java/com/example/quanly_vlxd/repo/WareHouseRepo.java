package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WareHouseRepo extends JpaRepository<Warehouse,Integer> {
}
