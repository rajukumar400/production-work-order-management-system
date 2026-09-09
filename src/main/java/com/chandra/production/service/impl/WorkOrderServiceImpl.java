package com.chandra.production.service.impl;

import com.chandra.production.dto.WorkOrderRequest;
import com.chandra.production.entity.Product;
import com.chandra.production.entity.WorkOrder;
import com.chandra.production.entity.WorkOrderPriority;
import com.chandra.production.entity.WorkOrderStatus;
import com.chandra.production.event.WorkOrderCreatedEvent;
import com.chandra.production.exception.ResourceNotFoundException;
import com.chandra.production.kafka.WorkOrderProducer;
import com.chandra.production.repository.ProductRepository;
import com.chandra.production.repository.WorkOrderRepository;
import com.chandra.production.service.WorkOrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final ProductRepository productRepository;
    private final WorkOrderProducer workOrderProducer;

    @Override
    @Transactional
    public WorkOrder createWorkOrder(WorkOrderRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: "
                                        + request.getProductId()));

        WorkOrder workOrder = WorkOrder.builder()
                .workOrderNumber(generateWorkOrderNumber())
                .product(product)
                .plannedQuantity(request.getPlannedQuantity())
                .completedQuantity(0.0)
                .status(WorkOrderStatus.CREATED)
                .priority(request.getPriority() != null
                        ? request.getPriority()
                        : WorkOrderPriority.NORMAL)
                .plannedStartDate(request.getPlannedStartDate())
                .plannedEndDate(request.getPlannedEndDate())
                .build();

        WorkOrder savedWorkOrder =
                workOrderRepository.save(workOrder);

        // Publish event to Kafka
        WorkOrderCreatedEvent event =
                new WorkOrderCreatedEvent(
                        savedWorkOrder.getId(),
                        product.getId(),
                        savedWorkOrder.getPlannedQuantity().intValue(),
                        savedWorkOrder.getStatus().name()
                );

        workOrderProducer.sendWorkOrderCreatedEvent(event);

        return savedWorkOrder;
    }

    @Override
    public WorkOrder getWorkOrderById(Long id) {

        return workOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Work Order not found with id: " + id));
    }

    @Override
    public WorkOrder getWorkOrderByNumber(String workOrderNumber) {

        return workOrderRepository
                .findByWorkOrderNumber(workOrderNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Work Order not found with number: "
                                        + workOrderNumber));
    }

    @Override
    public List<WorkOrder> getAllWorkOrders() {

        return workOrderRepository.findAll();
    }

    @Override
    public List<WorkOrder> getWorkOrdersByProductId(Long productId) {

        return workOrderRepository.findByProductId(productId);
    }

    @Override
    @Transactional
    public WorkOrder startWorkOrder(Long id) {

        WorkOrder workOrder = getWorkOrderById(id);

        if (workOrder.getStatus() != WorkOrderStatus.CREATED) {
            throw new IllegalStateException(
                    "Only CREATED work orders can be started");
        }

        workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);

        return workOrderRepository.save(workOrder);
    }

    @Override
    @Transactional
    public WorkOrder completeWorkOrder(Long id) {

        WorkOrder workOrder = getWorkOrderById(id);

        if (workOrder.getStatus() != WorkOrderStatus.IN_PROGRESS) {
            throw new IllegalStateException(
                    "Only IN_PROGRESS work orders can be completed");
        }

        workOrder.setCompletedQuantity(
                workOrder.getPlannedQuantity());

        workOrder.setStatus(WorkOrderStatus.COMPLETED);

        return workOrderRepository.save(workOrder);
    }

    @Override
    @Transactional
    public WorkOrder cancelWorkOrder(Long id) {

        WorkOrder workOrder = getWorkOrderById(id);

        if (workOrder.getStatus() == WorkOrderStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Completed work order cannot be cancelled");
        }

        workOrder.setStatus(WorkOrderStatus.CANCELLED);

        return workOrderRepository.save(workOrder);
    }

    private String generateWorkOrderNumber() {

        String workOrderNumber;

        do {
            workOrderNumber =
                    "WO-" + LocalDate.now().toString().replace("-", "")
                            + "-" + (int) (Math.random() * 9000 + 1000);

        } while (workOrderRepository
                .existsByWorkOrderNumber(workOrderNumber));

        return workOrderNumber;
    }
}