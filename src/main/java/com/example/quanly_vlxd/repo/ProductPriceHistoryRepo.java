package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.ProductPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPriceHistoryRepo extends JpaRepository<ProductPriceHistory, Integer> {

}
