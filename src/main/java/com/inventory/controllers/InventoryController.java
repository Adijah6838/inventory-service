package com.inventory.controllers;

import com.inventory.dtos.*;
import com.inventory.entities.*;
import com.inventory.services.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductRequest req) {
        return new ResponseEntity<>(inventoryService.createProduct(req), HttpStatus.CREATED);
    }

    @PostMapping("/warehouses")
    public ResponseEntity<Warehouse> createWarehouse(@Valid @RequestBody WarehouseRequest req) {
        return new ResponseEntity<>(inventoryService.createWarehouse(req), HttpStatus.CREATED);
    }

    @PostMapping("/stock")
    public ResponseEntity<Inventory> addStock(@Valid @RequestBody StockRequest req) {
        return new ResponseEntity<>(inventoryService.addStock(req), HttpStatus.CREATED);
    }

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveStock(@Valid @RequestBody ReservationRequest req) {
        inventoryService.reserveStock(req);
        return ResponseEntity.ok("Reservation successful");
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelReservation(@Valid @RequestBody CancelRequest req) {
        inventoryService.cancelReservation(req);
        return ResponseEntity.ok("Reservation cancelled successfully");
    }
}