

package com.chandra.production.controller;

import com.chandra.production.dto.ProductionRequest;
import com.chandra.production.entity.Production;
import com.chandra.production.service.ProductionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productions")
@RequiredArgsConstructor
public class ProductionController {

    private final ProductionService productionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION_MANAGER')")
    public ResponseEntity<Production> createProduction(
            @Valid @RequestBody ProductionRequest request) {

        Production production =
                productionService.createProduction(request);

        return new ResponseEntity<>(
                production,
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION_MANAGER')")
    public ResponseEntity<Production> getProductionById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productionService.getProductionById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION_MANAGER')")
    public ResponseEntity<List<Production>> getAllProductions() {

        return ResponseEntity.ok(
                productionService.getAllProductions());
    }

    @GetMapping("/work-order/{workOrderId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION_MANAGER')")
    public ResponseEntity<List<Production>>
    getProductionsByWorkOrderId(
            @PathVariable Long workOrderId) {

        return ResponseEntity.ok(
                productionService
                        .getProductionsByWorkOrderId(workOrderId));
    }

    @PutMapping("/{id}/start")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION_MANAGER')")
    public ResponseEntity<Production> startProduction(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productionService.startProduction(id));
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION_MANAGER')")
    public ResponseEntity<Production> completeProduction(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productionService.completeProduction(id));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCTION_MANAGER')")
    public ResponseEntity<Production> cancelProduction(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productionService.cancelProduction(id));
    }
}