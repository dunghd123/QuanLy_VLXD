package com.example.quanly_vlxd.service.impl;

import com.example.quanly_vlxd.dto.request.WarehouseRequest;
import com.example.quanly_vlxd.dto.response.MessageResponse;
import com.example.quanly_vlxd.dto.response.WarehouseResponse;
import com.example.quanly_vlxd.entity.OutputInvoiceDetail;
import com.example.quanly_vlxd.entity.WareHouse_Product;
import com.example.quanly_vlxd.entity.Warehouse;
import com.example.quanly_vlxd.repo.OutputInvoiceDetailRepo;
import com.example.quanly_vlxd.repo.WareHouseRepo;
import com.example.quanly_vlxd.repo.WarehouseProductRepo;
import com.example.quanly_vlxd.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WareHouseRepo wareHouseRepo;
    private final OutputInvoiceDetailRepo outputInvoiceDetailRepo;
    private final WarehouseProductRepo warehouseProductRepo;

    @Override
    public MessageResponse addWarehouse(WarehouseRequest warehouseRequest) {
        for(Warehouse w: wareHouseRepo.findAll()){
            if(warehouseRequest.getName().equals(w.getName())
                    && warehouseRequest.getLocation().equals(w.getLocation())){
                return MessageResponse.builder().message("Warehouse already exist!!!").build();
            }
        }
        Warehouse warehouse = Warehouse.builder()
                .Name(warehouseRequest.getName())
                .Location(warehouseRequest.getLocation())
                .IsActive(true)
                .build();
        wareHouseRepo.save(warehouse);
        return MessageResponse.builder().message("Create new warehouse successfully!!!").build();
    }

    @Override
    public MessageResponse updateWarehouse(int id, WarehouseRequest warehouseRequest) {
        Optional<Warehouse> warehouseCur = wareHouseRepo.findById(id);
        if(warehouseCur.isEmpty()){
            return MessageResponse.builder().message("Warehouse not found!!!").build();
        }
        List<Warehouse> findwarehouses = wareHouseRepo.findAll();
        findwarehouses.removeIf(w->w.getId()==id);
        for(Warehouse w: findwarehouses){
            if(warehouseRequest.getName().equals(w.getName())
                    && warehouseRequest.getLocation().equals(w.getLocation())){
                return MessageResponse.builder().message("Warehouse already exist!!!").build();
            }
        }
        Warehouse warehouse = warehouseCur.get();
        warehouse.setName(warehouseRequest.getName());
        warehouse.setLocation(warehouseRequest.getLocation());
        wareHouseRepo.save(warehouse);
        return null;
    }

    @Override
    public MessageResponse deleteWarehouse(int id) {
        Optional<Warehouse> warehouseCur = wareHouseRepo.findById(id);
        if(warehouseCur.isEmpty()){
            return MessageResponse.builder().message("Warehouse not found!!!").build();
        }
        for(OutputInvoiceDetail o: outputInvoiceDetailRepo.findAll()){
            if(o.getWarehouse().getId()==id){
                outputInvoiceDetailRepo.deleteById(o.getId());
            }
        }
        for(WareHouse_Product w: warehouseProductRepo.findAll()){
            if(w.getWarehouse().getId()==id){
                warehouseProductRepo.deleteById(w.getId());
            }
        }
        warehouseCur.get().setIsActive(false);
        wareHouseRepo.save(warehouseCur.get());
        return MessageResponse.builder().message("Delete warehouse with id: "+id+"  successfully!!!").build();
    }

    @Override
    public Page<WarehouseResponse> getList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return wareHouseRepo.getAll(pageable).map(this::convertToDTO);
    }
    private WarehouseResponse convertToDTO(Warehouse warehouse){
        return WarehouseResponse.builder()
                .id(warehouse.getId())
                .name(warehouse.getName())
                .location(warehouse.getLocation())
                .build();
    }
}
