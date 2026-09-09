package com.chandra.production.controller;

import com.chandra.production.dto.WorkOrderRequest;
import com.chandra.production.entity.WorkOrder;
import com.chandra.production.service.WorkOrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/work-orders")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION_MANAGER')")
    public ResponseEntity<WorkOrder> createWorkOrder(
            @Valid @RequestBody WorkOrderRequest request) {

        WorkOrder workOrder =
                workOrderService.createWorkOrder(request);

        return new ResponseEntity<>(
                workOrder,
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION_MANAGER')")
    public ResponseEntity<WorkOrder> getWorkOrderById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workOrderService.getWorkOrderById(id));
    }

    @GetMapping("/number/{workOrderNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION_MANAGER')")
    public ResponseEntity<WorkOrder> getWorkOrderByNumber(
            @PathVariable String workOrderNumber) {

        return ResponseEntity.ok(
                workOrderService
                        .getWorkOrderByNumber(workOrderNumber));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION_MANAGER')")
    public ResponseEntity<List<WorkOrder>> getAllWorkOrders() {

        return ResponseEntity.ok(
                workOrderService.getAllWorkOrders());
    }

    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION_MANAGER')")
    public ResponseEntity<List<WorkOrder>> getByProductId(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                workOrderService
                        .getWorkOrdersByProductId(productId));
    }

    @PutMapping("/{id}/start")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION_MANAGER')")
    public ResponseEntity<WorkOrder> startWorkOrder(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workOrderService.startWorkOrder(id));
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION_MANAGER')")
    public ResponseEntity<WorkOrder> completeWorkOrder(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workOrderService.completeWorkOrder(id));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION_MANAGER')")
    public ResponseEntity<WorkOrder> cancelWorkOrder(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                workOrderService.cancelWorkOrder(id));
    }
}