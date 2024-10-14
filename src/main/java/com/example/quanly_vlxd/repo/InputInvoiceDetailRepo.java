package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.InputInvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InputInvoiceDetailRepo extends JpaRepository<InputInvoiceDetail,Integer> {
}
