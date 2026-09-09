package com.chandra.production.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DispatchRequest {

    @NotNull(message = "Production ID is required")
    private Long productionId;

    @NotNull(message = "Dispatch quantity is required")
    @Positive(message = "Dispatch quantity must be greater than zero")
    private Double dispatchQuantity;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    private String destination;

    private String remarks;
}