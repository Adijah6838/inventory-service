package com.inventory.repositories;
import com.inventory.entities.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {}