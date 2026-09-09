
package com.chandra.production.service;

import com.chandra.production.dto.RawMaterialRequest;
import com.chandra.production.entity.RawMaterial;
import org.springframework.data.domain.Page;

public interface RawMaterialService {

    RawMaterial createRawMaterial(RawMaterialRequest request);

    RawMaterial getRawMaterialById(Long id);

    Page<RawMaterial> getAllRawMaterials(
            int page,
            int size,
            String sortBy,
            String direction
    );

    Page<RawMaterial> searchRawMaterials(
            String keyword,
            int page,
            int size
    );

    RawMaterial updateRawMaterial(
            Long id,
            RawMaterialRequest request
    );

    void deleteRawMaterial(Long id);
}