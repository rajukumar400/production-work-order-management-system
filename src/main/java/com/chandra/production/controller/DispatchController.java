package com.chandra.production.controller;

import com.chandra.production.dto.DispatchRequest;
import com.chandra.production.entity.Dispatch;
import com.chandra.production.service.DispatchService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dispatches")
@RequiredArgsConstructor
public class DispatchController {

    private final DispatchService dispatchService;

    // =========================================================
    // CREATE DISPATCH
    // =========================================================

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STORE_MANAGER')")
    public ResponseEntity<Dispatch> createDispatch(
            @Valid @RequestBody DispatchRequest request) {

        Dispatch dispatch =
                dispatchService.createDispatch(request);

        return new ResponseEntity<>(
                dispatch,
                HttpStatus.CREATED);
    }

    // =========================================================
    // SEARCH BY CUSTOMER NAME
    // =========================================================

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'STORE_MANAGER')")
    public ResponseEntity<List<Dispatch>> searchByCustomerName(
            @RequestParam String customerName) {

        return ResponseEntity.ok(
                dispatchService
                        .getDispatchesByCustomerName(customerName));
    }

    // =========================================================
    // FILTER BY STATUS
    // =========================================================

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STORE_MANAGER')")
    public ResponseEntity<List<Dispatch>> getDispatchesByStatus(
            @PathVariable String status) {

        return ResponseEntity.ok(
                dispatchService
                        .getDispatchesByStatus(status));
    }

    // =========================================================
    // GET DISPATCH BY ID
    // =========================================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STORE_MANAGER')")
    public ResponseEntity<Dispatch> getDispatchById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                dispatchService.getDispatchById(id));
    }

    // =========================================================
    // GET ALL DISPATCHES
    // =========================================================

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STORE_MANAGER')")
    public ResponseEntity<List<Dispatch>> getAllDispatches() {

        return ResponseEntity.ok(
                dispatchService.getAllDispatches());
    }

    // =========================================================
    // GET ALL DISPATCHES WITH PAGINATION
    // =========================================================

    @GetMapping("/paginated")
    @PreAuthorize("hasAnyRole('ADMIN', 'STORE_MANAGER')")
    public ResponseEntity<Page<Dispatch>> getAllDispatchesPaginated(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "createdAt,desc")
            String sort) {

        String[] sortParams = sort.split(",");

        Pageable pageable;

        if (sortParams.length == 2) {

            Sort.Direction direction =
                    Sort.Direction.fromString(sortParams[1]);

            pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by(
                            direction,
                            sortParams[0]
                    )
            );

        } else {

            pageable = PageRequest.of(
                    page,
                    size
            );
        }

        return ResponseEntity.ok(
                dispatchService.getAllDispatches(pageable));
    }

    // =========================================================
    // GET DISPATCHES BY PRODUCTION ID
    // =========================================================

    @GetMapping("/production/{productionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STORE_MANAGER')")
    public ResponseEntity<List<Dispatch>>
    getDispatchesByProductionId(
            @PathVariable Long productionId) {

        return ResponseEntity.ok(
                dispatchService
                        .getDispatchesByProductionId(productionId));
    }

    // =========================================================
    // MARK AS READY
    // =========================================================

    @PutMapping("/{id}/ready")
    @PreAuthorize("hasAnyRole('ADMIN', 'STORE_MANAGER')")
    public ResponseEntity<Dispatch> markAsReady(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                dispatchService.markAsReady(id));
    }

    // =========================================================
    // DISPATCH
    // =========================================================

    @PutMapping("/{id}/dispatch")
    @PreAuthorize("hasAnyRole('ADMIN', 'STORE_MANAGER')")
    public ResponseEntity<Dispatch> dispatch(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                dispatchService.dispatch(id));
    }

    // =========================================================
    // CANCEL DISPATCH
    // =========================================================

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'STORE_MANAGER')")
    public ResponseEntity<Dispatch> cancelDispatch(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                dispatchService.cancelDispatch(id));
    }
}