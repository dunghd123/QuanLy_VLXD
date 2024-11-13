package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {
    @Query(value = "select * from products p where p.isactive = 1",nativeQuery = true)
    Page<Product> getAll(Pageable pageable);
}
