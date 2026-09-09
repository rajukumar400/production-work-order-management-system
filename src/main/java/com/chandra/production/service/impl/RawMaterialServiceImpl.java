
package com.chandra.production.service.impl;

import com.chandra.production.dto.RawMaterialRequest;
import com.chandra.production.entity.RawMaterial;
import com.chandra.production.exception.DuplicateResourceException;
import com.chandra.production.exception.ResourceNotFoundException;
import com.chandra.production.repository.RawMaterialRepository;
import com.chandra.production.service.RawMaterialService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RawMaterialServiceImpl implements RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;

    @Override
    public RawMaterial createRawMaterial(
            RawMaterialRequest request) {

        if (rawMaterialRepository.existsByMaterialCode(
                request.getMaterialCode())) {

            throw new DuplicateResourceException(
                    "Raw material already exists with code: "
                            + request.getMaterialCode());
        }

        RawMaterial rawMaterial = RawMaterial.builder()
                .materialCode(request.getMaterialCode())
                .name(request.getName())
                .category(request.getCategory())
                .description(request.getDescription())
                .unit(request.getUnit())
                .unitPrice(request.getUnitPrice())
                .minimumStock(request.getMinimumStock())
                .currentStock(
                        request.getCurrentStock() != null
                                ? request.getCurrentStock()
                                : 0.0)
                .active(true)
                .build();

        return rawMaterialRepository.save(rawMaterial);
    }

    @Override
    public RawMaterial getRawMaterialById(Long id) {

        return rawMaterialRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Raw material not found with id: " + id));
    }

    @Override
    public Page<RawMaterial> getAllRawMaterials(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        return rawMaterialRepository.findAll(pageable);
    }

    @Override
    public Page<RawMaterial> searchRawMaterials(
            String keyword,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("name").ascending());

        return rawMaterialRepository
                .findByNameContainingIgnoreCase(
                        keyword,
                        pageable);
    }

    @Override
    public RawMaterial updateRawMaterial(
            Long id,
            RawMaterialRequest request) {

        RawMaterial rawMaterial =
                rawMaterialRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Raw material not found with id: "
                                                + id));

        rawMaterial.setMaterialCode(
                request.getMaterialCode());

        rawMaterial.setName(
                request.getName());

        rawMaterial.setCategory(
                request.getCategory());

        rawMaterial.setDescription(
                request.getDescription());

        rawMaterial.setUnit(
                request.getUnit());

        rawMaterial.setUnitPrice(
                request.getUnitPrice());

        rawMaterial.setMinimumStock(
                request.getMinimumStock());

        if (request.getCurrentStock() != null) {
            rawMaterial.setCurrentStock(
                    request.getCurrentStock());
        }

        return rawMaterialRepository.save(rawMaterial);
    }

    @Override
    public void deleteRawMaterial(Long id) {

        RawMaterial rawMaterial =
                rawMaterialRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Raw material not found with id: "
                                                + id));

        rawMaterialRepository.delete(rawMaterial);
    }
}