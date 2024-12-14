package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.InputInvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InputInvoiceDetailRepo extends JpaRepository<InputInvoiceDetail,Integer> {
    List<InputInvoiceDetail> findByInputInvoiceId(int id);

    @Query(value = "select * from input_invoice_details i where i.pro_id in ?1", nativeQuery = true)
    List<InputInvoiceDetail> filterByProduct(List<Integer> productIds);
}
