package com.example.quanly_vlxd.repo;


import com.example.quanly_vlxd.entity.OutputInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OutputInvoiceRepo extends JpaRepository<OutputInvoice,Integer> {
    @Query(value = "select * from output_invoices o where o.isactive = 1 and o.oi_id = ?1",nativeQuery = true)
    Optional<OutputInvoice> findByOutputInvoiceId(int id);
}
