package com.example.quanly_vlxd.repo;

import com.example.quanly_vlxd.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Integer> {
    @Query(value = "select * from categories c where c.isactive = 1",nativeQuery = true)
    Page<Category> getAll(Pageable pageable);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Integer id);
    @Query(value = "select * from categories c where c.isactive = 1",nativeQuery = true)
    List<Category> getListCategory();
}
