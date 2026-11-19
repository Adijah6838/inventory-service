package com.inventory.dtos;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
public class StockRequest {
    @NotNull
    private UUID productId;
    @NotNull
    private UUID warehouseId;
    @NotNull @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;
    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }
    public UUID getWarehouseId() { return warehouseId; }
    public void setWarehouseId(UUID warehouseId) { this.warehouseId = warehouseId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}