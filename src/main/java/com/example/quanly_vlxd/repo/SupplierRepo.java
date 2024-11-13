package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepo extends JpaRepository<Supplier,Integer> {
    @Query(value = "select * from suppliers s where s.isactive = 1",nativeQuery = true)
    Page<Supplier> getAll(Pageable pageable);
}
