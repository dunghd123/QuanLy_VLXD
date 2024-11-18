package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.ProductPriceHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceHistoryRepo extends JpaRepository<ProductPriceHistory,Integer> {
    @Query(value = "select * from product_price_history",nativeQuery = true)
    Page<ProductPriceHistory> getAll(Pageable pageable);

    @Query(value = "SELECT *  FROM product_price_history p WHERE p.pro_id = :productId AND p.isactive = 1 AND p.pph_invoice_type like :type", nativeQuery = true)
    Optional<ProductPriceHistory> findActivePriceByProductId(@Param("productId") int productId,@Param("type") String type );
}
