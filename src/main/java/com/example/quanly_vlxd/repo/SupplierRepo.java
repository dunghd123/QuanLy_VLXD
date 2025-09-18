package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepo extends JpaRepository<Supplier,Integer> {
    @Query(value = "select * from suppliers s where s.isactive = 1",nativeQuery = true)
    Page<Supplier> getAll(Pageable pageable);

    boolean existsByPhoneNum(String phoneNum);
    boolean existsByPhoneNumAndIdNot(String phoneNum, Integer id);
    boolean existsByNameAndAddress(String name, String address);
    boolean existsByNameAndAddressAndIdNot(String name, String address, Integer id);
    @Override
    @Query(value = "select * from suppliers s where s.sup_id = ?1 and s.isactive = 1",nativeQuery = true)
    Optional<Supplier> findById(Integer integer);
}
