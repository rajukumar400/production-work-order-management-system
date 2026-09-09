package com.chandra.production.repository;



import com.chandra.production.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByRawMaterialId(Long rawMaterialId);

    boolean existsByRawMaterialId(Long rawMaterialId);
}