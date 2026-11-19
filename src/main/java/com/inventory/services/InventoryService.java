package com.inventory.services;

import com.inventory.dtos.*;
import com.inventory.entities.*;
import com.inventory.exceptions.InsufficientStockException;
import com.inventory.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {
    private final ProductRepository productRepo;
    private final WarehouseRepository warehouseRepo;
    private final InventoryRepository inventoryRepo;

    public InventoryService(ProductRepository productRepo, WarehouseRepository warehouseRepo, InventoryRepository inventoryRepo) {
        this.productRepo = productRepo;
        this.warehouseRepo = warehouseRepo;
        this.inventoryRepo = inventoryRepo;
    }

    @Transactional
    public Product createProduct(ProductRequest req) {
        Product product = new Product();
        product.setSku(req.getSku());
        product.setName(req.getName());
        return productRepo.save(product);
    }

    @Transactional
    public Warehouse createWarehouse(WarehouseRequest req) {
        Warehouse warehouse = new Warehouse();
        warehouse.setName(req.getName());
        warehouse.setLocation(req.getLocation());
        return warehouseRepo.save(warehouse);
    }

    @Transactional
    public Inventory addStock(StockRequest req) {
        Product product = productRepo.findById(req.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        Warehouse warehouse = warehouseRepo.findById(req.getWarehouseId()).orElseThrow(() -> new RuntimeException("Warehouse not found"));
        List<Inventory> existing = inventoryRepo.findAvailableStockWithLock(req.getProductId(), 0);
        for (Inventory inv : existing) {
            if (inv.getWarehouse().getId().equals(req.getWarehouseId())) {
                inv.setQuantity(inv.getQuantity() + req.getQuantity());
                return inventoryRepo.save(inv);
            }
        }
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setWarehouse(warehouse);
        inventory.setQuantity(req.getQuantity());
        return inventoryRepo.save(inventory);
    }

    @Transactional
    public void reserveStock(ReservationRequest req) {
        List<Inventory> availableInventories = inventoryRepo.findAvailableStockWithLock(req.getProductId(), req.getQuantity());
        int remainingToReserve = req.getQuantity();
        for (Inventory inv : availableInventories) {
            if (remainingToReserve == 0) break;
            int take = Math.min(inv.getQuantity(), remainingToReserve);
            inv.setQuantity(inv.getQuantity() - take);
            remainingToReserve -= take;
            inventoryRepo.save(inv);
        }
        if (remainingToReserve > 0) {
            throw new InsufficientStockException("Not enough stock available. Missing: " + remainingToReserve);
        }
    }

    @Transactional
    public void cancelReservation(CancelRequest req) {
        List<Inventory> inventories = inventoryRepo.findAvailableStockWithLock(req.getProductId(), 0);
        for (Inventory inv : inventories) {
            if (inv.getWarehouse().getId().equals(req.getWarehouseId())) {
                inv.setQuantity(inv.getQuantity() + req.getQuantity());
                inventoryRepo.save(inv);
                return;
            }
        }
        throw new RuntimeException("Inventory record not found to cancel");
    }
}