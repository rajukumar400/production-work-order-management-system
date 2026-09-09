package com.chandra.production.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductionRequest {

    @NotNull(message = "Work order ID is required")
    private Long workOrderId;

    @NotNull(message = "Production quantity is required")
    @Positive(message = "Production quantity must be greater than zero")
    private Double productionQuantity;

    @Positive(message = "Actual quantity must be greater than zero")
    private Double actualQuantity;

    private LocalDateTime productionDate;
}