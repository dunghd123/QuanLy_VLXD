package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.WareHouse_Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseProductRepo extends JpaRepository<WareHouse_Product,Integer> {
    @Query(value = "select * from warehouse_products wp where wp.wh_id = ?1 and wp.pro_id = ?2",nativeQuery = true)
    Optional<WareHouse_Product> findByWarehouseAndProduct(int warehouse, int product);
}
