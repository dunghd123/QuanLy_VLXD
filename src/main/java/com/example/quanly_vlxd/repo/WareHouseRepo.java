package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WareHouseRepo extends JpaRepository<Warehouse,Integer> {
    @Query(value = "select * from warehouses w where w.isactive = 1",nativeQuery = true)
    Page<Warehouse> getAll(Pageable pageable);
}
