

package com.chandra.production.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawMaterialRequest {

    @NotBlank(message = "Material code is required")
    private String materialCode;

    @NotBlank(message = "Material name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    private String description;

    @NotBlank(message = "Unit is required")
    private String unit;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Unit price must be greater than 0")
    private Double unitPrice;

    @NotNull(message = "Minimum stock is required")
    @DecimalMin(value = "0.0",
            message = "Minimum stock cannot be negative")
    private Double minimumStock;

    @DecimalMin(value = "0.0",
            message = "Current stock cannot be negative")
    private Double currentStock;
}