package com.chandra.production.controller;

import org.springframework.security.access.prepost.PreAuthorize;

import com.chandra.production.dto.InventoryRequest;
import com.chandra.production.entity.Inventory;
import com.chandra.production.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventory(
            @Valid @RequestBody InventoryRequest request) {

        Inventory inventory = inventoryService.createInventory(request);

        return new ResponseEntity<>(inventory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_MANAGER')")
    public ResponseEntity<Inventory> getInventoryById(
            @PathVariable Long id) {

        Inventory inventory = inventoryService.getInventoryById(id);

        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/raw-material/{rawMaterialId}")
    public ResponseEntity<Inventory> getInventoryByRawMaterialId(
            @PathVariable Long rawMaterialId) {

        Inventory inventory =
                inventoryService.getInventoryByRawMaterialId(rawMaterialId);

        return ResponseEntity.ok(inventory);
    }

    @PutMapping("/stock-in/{rawMaterialId}")
    public ResponseEntity<Inventory> stockIn(
            @PathVariable Long rawMaterialId,
            @RequestParam Double quantity) {

        Inventory inventory =
                inventoryService.stockIn(rawMaterialId, quantity);

        return ResponseEntity.ok(inventory);
    }

    @PutMapping("/stock-out/{rawMaterialId}")
    public ResponseEntity<Inventory> stockOut(
            @PathVariable Long rawMaterialId,
            @RequestParam Double quantity) {

        Inventory inventory =
                inventoryService.stockOut(rawMaterialId, quantity);

        return ResponseEntity.ok(inventory);
    }
}