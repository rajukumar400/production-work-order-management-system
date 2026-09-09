package com.chandra.production.service;


import com.chandra.production.dto.WorkOrderRequest;
import com.chandra.production.entity.WorkOrder;

import java.util.List;

public interface WorkOrderService {

    WorkOrder createWorkOrder(WorkOrderRequest request);

    WorkOrder getWorkOrderById(Long id);

    WorkOrder getWorkOrderByNumber(String workOrderNumber);

    List<WorkOrder> getAllWorkOrders();

    List<WorkOrder> getWorkOrdersByProductId(Long productId);

    WorkOrder startWorkOrder(Long id);

    WorkOrder completeWorkOrder(Long id);

    WorkOrder cancelWorkOrder(Long id);
}