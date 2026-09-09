package com.chandra.production.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QualityCheckRequest {

    @NotNull(message = "Production ID is required")
    private Long productionId;

    @NotNull(message = "Checked quantity is required")
    @Positive(message = "Checked quantity must be greater than zero")
    private Double checkedQuantity;

    @PositiveOrZero(message = "Passed quantity cannot be negative")
    private Double passedQuantity;

    @PositiveOrZero(message = "Failed quantity cannot be negative")
    private Double failedQuantity;

    private String remarks;
}