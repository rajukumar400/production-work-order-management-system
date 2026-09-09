package com.chandra.production.repository;

import com.chandra.production.entity.RawMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawMaterialRepository
        extends JpaRepository<RawMaterial, Long> {

    boolean existsByMaterialCode(String materialCode);

    Page<RawMaterial> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable);

    Page<RawMaterial> findByCategoryContainingIgnoreCase(
            String category,
            Pageable pageable);
}