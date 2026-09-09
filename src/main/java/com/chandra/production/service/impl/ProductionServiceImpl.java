package com.chandra.production.service.impl;




import com.chandra.production.dto.ProductionRequest;
import com.chandra.production.entity.Production;
import com.chandra.production.entity.ProductionStatus;
import com.chandra.production.entity.WorkOrder;
import com.chandra.production.exception.ResourceNotFoundException;
import com.chandra.production.repository.ProductionRepository;
import com.chandra.production.repository.WorkOrderRepository;
import com.chandra.production.service.ProductionService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductionServiceImpl implements ProductionService {

    private final ProductionRepository productionRepository;
    private final WorkOrderRepository workOrderRepository;

    @Override
    @Transactional
    public Production createProduction(ProductionRequest request) {

        WorkOrder workOrder = workOrderRepository
                .findById(request.getWorkOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Work Order not found with id: "
                                        + request.getWorkOrderId()));

        if (workOrder.getStatus().name().equals("CANCELLED")) {
            throw new RuntimeException(
                    "Production cannot be created for cancelled work order");
        }

        Production production = Production.builder()
                .workOrder(workOrder)
                .productionQuantity(request.getProductionQuantity())
                .actualQuantity(
                        request.getActualQuantity() != null
                                ? request.getActualQuantity()
                                : 0.0)
                .status(ProductionStatus.PLANNED)
                .productionDate(request.getProductionDate())
                .build();

        return productionRepository.save(production);
    }

    @Override
    public Production getProductionById(Long id) {

        return productionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Production not found with id: " + id));
    }

    @Override
    public List<Production> getAllProductions() {

        return productionRepository.findAll();
    }

    @Override
    public List<Production> getProductionsByWorkOrderId(
            Long workOrderId) {

        return productionRepository
                .findByWorkOrderId(workOrderId);
    }

    @Override
    @Transactional
    public Production startProduction(Long id) {

        Production production = getProductionById(id);

        if (production.getStatus() != ProductionStatus.PLANNED) {
            throw new RuntimeException(
                    "Only PLANNED production can be started");
        }

        production.setStatus(ProductionStatus.IN_PROGRESS);

        return productionRepository.save(production);
    }

    @Override
    @Transactional
    public Production completeProduction(Long id) {

        Production production = getProductionById(id);

        if (production.getStatus() != ProductionStatus.IN_PROGRESS) {
            throw new RuntimeException(
                    "Only IN_PROGRESS production can be completed");
        }

        if (production.getActualQuantity() <= 0) {
            throw new RuntimeException(
                    "Actual quantity must be greater than zero");
        }

        production.setStatus(ProductionStatus.COMPLETED);

        return productionRepository.save(production);
    }

    @Override
    @Transactional
    public Production cancelProduction(Long id) {

        Production production = getProductionById(id);

        if (production.getStatus() == ProductionStatus.COMPLETED) {
            throw new RuntimeException(
                    "Completed production cannot be cancelled");
        }

        production.setStatus(ProductionStatus.CANCELLED);

        return productionRepository.save(production);
    }
}