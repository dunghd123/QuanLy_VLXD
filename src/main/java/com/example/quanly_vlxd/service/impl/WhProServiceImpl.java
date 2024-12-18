package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.entity.Product;
import com.example.quanly_vlxd.entity.WareHouse_Product;
import com.example.quanly_vlxd.entity.Warehouse;
import com.example.quanly_vlxd.repo.ProductRepo;
import com.example.quanly_vlxd.repo.WareHouseRepo;
import com.example.quanly_vlxd.repo.WarehouseProductRepo;
import com.example.quanly_vlxd.service.WhProService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WhProServiceImpl implements WhProService {
    private final WarehouseProductRepo whProRepo;
    private final ProductRepo proRepo;
    private final WareHouseRepo whRepo;
    @Override
    public MessageResponse AddAllProductFirstTime() {
        for (Product p: proRepo.findAll()) {
            for (Warehouse w : whRepo.findAll()) {
                WareHouse_Product whPro = WareHouse_Product.builder()
                        .product(p)
                        .warehouse(w)
                        .Quantity(0)
                        .LastUpdated(new Date())
                        .build();
                whProRepo.save(whPro);
            }
        }
        return MessageResponse.builder().message("Add all product first time successfully!!!").build();
    }

    @Override
    public MessageResponse addProduct(int id, int whId) {
        Optional<Product> product = proRepo.findById(id);
        if (product.isEmpty())
            return MessageResponse.builder().message("Product ID: " + id + " is not exist").build();
        Optional<Warehouse> warehouse = whRepo.findById(whId);
        if (warehouse.isEmpty())
            return MessageResponse.builder().message("Warehouse ID: " + whId + " is not exist").build();
        WareHouse_Product whPro = WareHouse_Product.builder()
                .product(product.get())
                .warehouse(warehouse.get())
                .Quantity(0)
                .LastUpdated(new Date())
                .build();
        whProRepo.save(whPro);
        return MessageResponse.builder().message("Add product successfully!!!").build();
    }
}
