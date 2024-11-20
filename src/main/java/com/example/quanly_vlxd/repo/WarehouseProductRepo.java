package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.WareHouse_Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseProductRepo extends JpaRepository<WareHouse_Product,Integer> {
}
