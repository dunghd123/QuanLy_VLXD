package com.example.quanly_vlxd.repo;


import com.example.quanly_vlxd.entity.OutputInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutputInvoiceRepo extends JpaRepository<OutputInvoice,String> {
}
