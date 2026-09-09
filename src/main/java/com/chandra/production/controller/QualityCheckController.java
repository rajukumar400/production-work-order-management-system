package com.chandra.production.controller;


import com.chandra.production.dto.QualityCheckRequest;
import com.chandra.production.entity.QualityCheck;
import com.chandra.production.service.QualityCheckService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quality-checks")
@RequiredArgsConstructor
public class QualityCheckController {

    private final QualityCheckService qualityCheckService;

    @PostMapping
    public ResponseEntity<QualityCheck> createQualityCheck(
            @Valid @RequestBody QualityCheckRequest request) {

        QualityCheck qualityCheck =
                qualityCheckService.createQualityCheck(request);

        return new ResponseEntity<>(
                qualityCheck,
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QualityCheck> getQualityCheckById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                qualityCheckService.getQualityCheckById(id));
    }

    @GetMapping
    public ResponseEntity<List<QualityCheck>>
    getAllQualityChecks() {

        return ResponseEntity.ok(
                qualityCheckService.getAllQualityChecks());
    }

    @GetMapping("/production/{productionId}")
    public ResponseEntity<List<QualityCheck>>
    getQualityChecksByProductionId(
            @PathVariable Long productionId) {

        return ResponseEntity.ok(
                qualityCheckService
                        .getQualityChecksByProductionId(productionId));
    }

    @PutMapping("/{id}/pass")
    public ResponseEntity<QualityCheck> passQualityCheck(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                qualityCheckService.passQualityCheck(id));
    }

    @PutMapping("/{id}/fail")
    public ResponseEntity<QualityCheck> failQualityCheck(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                qualityCheckService.failQualityCheck(id));
    }
}