package com.chandra.production.service.impl;

import com.chandra.production.dto.InventoryRequest;
import com.chandra.production.entity.Inventory;
import com.chandra.production.entity.RawMaterial;
import com.chandra.production.repository.InventoryRepository;
import com.chandra.production.repository.RawMaterialRepository;
import com.chandra.production.service.InventoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final RawMaterialRepository rawMaterialRepository;

    @Override
    @Transactional
    public Inventory createInventory(InventoryRequest request) {

        // Check whether inventory already exists
        if (inventoryRepository.existsByRawMaterialId(request.getRawMaterialId())) {
            throw new RuntimeException(
                    "Inventory already exists for raw material id: "
                            + request.getRawMaterialId());
        }

        // Find raw material
        RawMaterial rawMaterial = rawMaterialRepository
                .findById(request.getRawMaterialId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Raw material not found with id: "
                                        + request.getRawMaterialId()));

        // Create inventory
        Inventory inventory = Inventory.builder()
                .rawMaterial(rawMaterial)
                .availableQuantity(request.getAvailableQuantity())
                .reorderLevel(request.getReorderLevel())
                .reservedQuantity(0.0)
                .active(true)
                .build();

        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory getInventoryById(Long id) {

        return inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Inventory not found with id: " + id));
    }

    @Override
    public Inventory getInventoryByRawMaterialId(Long rawMaterialId) {

        return inventoryRepository.findByRawMaterialId(rawMaterialId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Inventory not found for raw material id: "
                                        + rawMaterialId));
    }

    @Override
    @Transactional
    public Inventory stockIn(Long rawMaterialId, Double quantity) {

        if (quantity == null || quantity <= 0) {
            throw new RuntimeException(
                    "Stock-in quantity must be greater than zero");
        }

        Inventory inventory = inventoryRepository
                .findByRawMaterialId(rawMaterialId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Inventory not found for raw material id: "
                                        + rawMaterialId));

        inventory.setAvailableQuantity(
                inventory.getAvailableQuantity() + quantity);

        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public Inventory stockOut(Long rawMaterialId, Double quantity) {

        if (quantity == null || quantity <= 0) {
            throw new RuntimeException(
                    "Stock-out quantity must be greater than zero");
        }

        Inventory inventory = inventoryRepository
                .findByRawMaterialId(rawMaterialId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Inventory not found for raw material id: "
                                        + rawMaterialId));

        if (inventory.getAvailableQuantity() < quantity) {
            throw new RuntimeException(
                    "Insufficient inventory. Available quantity: "
                            + inventory.getAvailableQuantity());
        }

        inventory.setAvailableQuantity(
                inventory.getAvailableQuantity() - quantity);

        return inventoryRepository.save(inventory);
    }
}