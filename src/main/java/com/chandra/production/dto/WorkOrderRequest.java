package com.chandra.production.dto;


import com.chandra.production.entity.WorkOrderPriority;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WorkOrderRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Planned quantity is required")
    @Positive(message = "Planned quantity must be greater than zero")
    private Double plannedQuantity;

    private WorkOrderPriority priority;

    private LocalDateTime plannedStartDate;

    private LocalDateTime plannedEndDate;
}