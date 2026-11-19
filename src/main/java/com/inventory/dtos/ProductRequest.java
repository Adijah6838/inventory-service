package com.inventory.dtos;
import jakarta.validation.constraints.NotBlank;
public class ProductRequest {
    @NotBlank(message = "SKU is required")
    private String sku;
    @NotBlank(message = "Name is required")
    private String name;
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}