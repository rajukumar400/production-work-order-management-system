package com.chandra.production.event;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderCreatedEvent {

    private Long workOrderId;

    private Long productId;

    private Integer quantity;

    private String status;
}
