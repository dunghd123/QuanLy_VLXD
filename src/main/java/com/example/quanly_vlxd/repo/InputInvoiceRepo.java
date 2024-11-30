package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.InputInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InputInvoiceRepo extends JpaRepository<InputInvoice,Integer> {
    @Query(value = "select * from input_invoices i where i.isactive = 1 and i.inp_id = ?1",nativeQuery = true)
    Optional<InputInvoice> findByInputID(int  id);
}
