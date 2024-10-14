package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.StatusInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusInvoiceRepo extends JpaRepository<StatusInvoice,Integer> {
}
