package com.chandra.production.Service;


import com.chandra.production.dto.RawMaterialRequest;
import com.chandra.production.entity.RawMaterial;
import com.chandra.production.exception.DuplicateResourceException;
import com.chandra.production.exception.ResourceNotFoundException;
import com.chandra.production.repository.RawMaterialRepository;
import com.chandra.production.service.impl.RawMaterialServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RawMaterialServiceImplTest {

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private RawMaterialServiceImpl rawMaterialService;


    @Test
    void createRawMaterial_shouldCreateSuccessfully() {

        RawMaterialRequest request =
                RawMaterialRequest.builder()
                        .materialCode("RM-1002")
                        .name("Copper Wire")
                        .category("Electrical")
                        .description("Industrial copper wire")
                        .unit("KG")
                        .unitPrice(720.0)
                        .minimumStock(50.0)
                        .currentStock(200.0)
                        .build();

        when(rawMaterialRepository
                .existsByMaterialCode("RM-1002"))
                .thenReturn(false);

        RawMaterial savedMaterial =
                RawMaterial.builder()
                        .id(2L)
                        .materialCode("RM-1002")
                        .name("Copper Wire")
                        .category("Electrical")
                        .unit("KG")
                        .unitPrice(720.0)
                        .minimumStock(50.0)
                        .currentStock(200.0)
                        .active(true)
                        .build();

        when(rawMaterialRepository
                .save(any(RawMaterial.class)))
                .thenReturn(savedMaterial);

        RawMaterial result =
                rawMaterialService.createRawMaterial(request);

        assertNotNull(result);
        assertEquals("RM-1002", result.getMaterialCode());
        assertEquals("Copper Wire", result.getName());

        verify(rawMaterialRepository)
                .save(any(RawMaterial.class));
    }


    @Test
    void createRawMaterial_duplicateCode_shouldFail() {

        RawMaterialRequest request =
                RawMaterialRequest.builder()
                        .materialCode("RM-1001")
                        .name("Steel Sheet")
                        .category("Metal")
                        .unit("KG")
                        .unitPrice(85.5)
                        .minimumStock(100.0)
                        .currentStock(500.0)
                        .build();

        when(rawMaterialRepository
                .existsByMaterialCode("RM-1001"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> rawMaterialService.createRawMaterial(request)
        );

        verify(rawMaterialRepository, never())
                .save(any(RawMaterial.class));
    }


    @Test
    void getRawMaterialById_shouldReturnMaterial() {

        RawMaterial material =
                RawMaterial.builder()
                        .id(1L)
                        .materialCode("RM-1001")
                        .name("Steel Sheet")
                        .category("Metal")
                        .unit("KG")
                        .unitPrice(85.5)
                        .currentStock(500.0)
                        .build();

        when(rawMaterialRepository.findById(1L))
                .thenReturn(Optional.of(material));

        RawMaterial result =
                rawMaterialService.getRawMaterialById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(
                "Steel Sheet",
                result.getName());

        verify(rawMaterialRepository)
                .findById(1L);
    }


    @Test
    void getRawMaterialById_notFound_shouldFail() {

        when(rawMaterialRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> rawMaterialService
                        .getRawMaterialById(99L)
        );

        verify(rawMaterialRepository)
                .findById(99L);
    }


    @Test
    void deleteRawMaterial_shouldDeleteSuccessfully() {

        RawMaterial material =
                RawMaterial.builder()
                        .id(1L)
                        .materialCode("RM-1001")
                        .name("Steel Sheet")
                        .build();

        when(rawMaterialRepository.findById(1L))
                .thenReturn(Optional.of(material));

        rawMaterialService.deleteRawMaterial(1L);

        verify(rawMaterialRepository)
                .delete(material);
    }
}