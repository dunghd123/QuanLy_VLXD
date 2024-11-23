package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.entity.Product;
import com.example.quanly_vlxd.entity.WareHouse_Product;
import com.example.quanly_vlxd.entity.Warehouse;
import com.example.quanly_vlxd.repo.ProductRepo;
import com.example.quanly_vlxd.repo.WareHouseRepo;
import com.example.quanly_vlxd.repo.WarehouseProductRepo;
import com.example.quanly_vlxd.service.WhProService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WhProServiceImpl implements WhProService {
    private final WarehouseProductRepo whProRepo;
    private final ProductRepo proRepo;
    private final WareHouseRepo whRepo;
    @Override
    public void AddAllProductFirstTime() {
        for (Product p: proRepo.findAll()) {
            for (Warehouse w : whRepo.findAll()) {
                WareHouse_Product whPro = WareHouse_Product.builder()
                        .product(p)
                        .warehouse(w)
                        .Quantity(0)
                        .LastUpdated(null)
                        .build();
                whProRepo.save(whPro);
            }
        }
    }
}
