package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {
    @Query(value = "select * from products s where s.isactive = 1",nativeQuery = true)
    Page<Product> getAll(Pageable pageable);

    List<Product> findByIdIn(List<Integer> productIds);
}
