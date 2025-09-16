package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.ProductPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceHistoryRepo extends JpaRepository<ProductPriceHistory,Integer>, JpaSpecificationExecutor<ProductPriceHistory> {

    @Query(value = "SELECT *  FROM product_price_history p WHERE p.pro_id = :productId AND p.isactive = 1 AND p.pph_invoice_type like :type", nativeQuery = true)
    Optional<ProductPriceHistory> findActivePriceByProductId(@Param("productId") int productId,@Param("type") String type );

}
