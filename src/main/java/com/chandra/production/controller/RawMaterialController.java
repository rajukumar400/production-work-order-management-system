package com.chandra.production.controller;

import com.chandra.production.dto.RawMaterialRequest;
import com.chandra.production.entity.RawMaterial;
import com.chandra.production.response.ApiResponse;
import com.chandra.production.service.RawMaterialService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/raw-materials")
@RequiredArgsConstructor
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    // =========================
    // CREATE RAW MATERIAL
    // =========================
    @PostMapping
    public ResponseEntity<ApiResponse<RawMaterial>> createRawMaterial(
            @Valid @RequestBody RawMaterialRequest request) {

        RawMaterial rawMaterial =
                rawMaterialService.createRawMaterial(request);

        ApiResponse<RawMaterial> response =
                new ApiResponse<>(
                        true,
                        "Raw material created successfully",
                        rawMaterial
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    // =========================
    // GET RAW MATERIAL BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RawMaterial>> getRawMaterialById(
            @PathVariable Long id) {

        RawMaterial rawMaterial =
                rawMaterialService.getRawMaterialById(id);

        ApiResponse<RawMaterial> response =
                new ApiResponse<>(
                        true,
                        "Raw material fetched successfully",
                        rawMaterial
                );

        return ResponseEntity.ok(response);
    }

    // =========================
    // GET ALL RAW MATERIALS
    // =========================
    @GetMapping
    public ResponseEntity<ApiResponse<Page<RawMaterial>>> getAllRawMaterials(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction) {

        Page<RawMaterial> rawMaterials =
                rawMaterialService.getAllRawMaterials(
                        page,
                        size,
                        sortBy,
                        direction
                );

        ApiResponse<Page<RawMaterial>> response =
                new ApiResponse<>(
                        true,
                        "Raw materials fetched successfully",
                        rawMaterials
                );

        return ResponseEntity.ok(response);
    }

    // =========================
    // SEARCH RAW MATERIALS
    // =========================
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<RawMaterial>>> searchRawMaterials(

            @RequestParam String keyword,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size) {

        Page<RawMaterial> rawMaterials =
                rawMaterialService.searchRawMaterials(
                        keyword,
                        page,
                        size
                );

        ApiResponse<Page<RawMaterial>> response =
                new ApiResponse<>(
                        true,
                        "Raw materials search successful",
                        rawMaterials
                );

        return ResponseEntity.ok(response);
    }

    // =========================
    // UPDATE RAW MATERIAL
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RawMaterial>> updateRawMaterial(

            @PathVariable Long id,

            @Valid @RequestBody RawMaterialRequest request) {

        RawMaterial rawMaterial =
                rawMaterialService.updateRawMaterial(
                        id,
                        request
                );

        ApiResponse<RawMaterial> response =
                new ApiResponse<>(
                        true,
                        "Raw material updated successfully",
                        rawMaterial
                );

        return ResponseEntity.ok(response);
    }

    // =========================
    // DELETE RAW MATERIAL
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRawMaterial(
            @PathVariable Long id) {

        rawMaterialService.deleteRawMaterial(id);

        ApiResponse<Void> response =
                new ApiResponse<>(
                        true,
                        "Raw material deleted successfully",
                        null
                );

        return ResponseEntity.ok(response);
    }
}