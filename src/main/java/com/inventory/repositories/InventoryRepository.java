package com.inventory.repositories;
import com.inventory.entities.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Inventory i WHERE i.product.id = :productId AND i.quantity >= :requiredQty ORDER BY i.quantity DESC")
    List<Inventory> findAvailableStockWithLock(@Param("productId") UUID productId, @Param("requiredQty") Integer requiredQty);
}