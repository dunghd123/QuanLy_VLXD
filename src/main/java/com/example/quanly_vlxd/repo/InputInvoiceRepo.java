package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.Employee;
import com.example.quanly_vlxd.entity.InputInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InputInvoiceRepo extends JpaRepository<InputInvoice,String> {
}
