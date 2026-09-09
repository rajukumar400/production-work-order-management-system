package com.chandra.production.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryRequest {

    @NotNull(message = "Raw material ID is required")
    private Long rawMaterialId;

    @NotNull(message = "Available quantity is required")
    @DecimalMin(value = "0.0", message = "Available quantity cannot be negative")
    private Double availableQuantity;

    @NotNull(message = "Reorder level is required")
    @DecimalMin(value = "0.0", message = "Reorder level cannot be negative")
    private Double reorderLevel;
}
