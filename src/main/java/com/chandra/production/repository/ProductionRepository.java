package com.chandra.production.repository;


import com.chandra.production.entity.Production;
import com.chandra.production.entity.ProductionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductionRepository
        extends JpaRepository<Production, Long> {

    List<Production> findByStatus(ProductionStatus status);

    List<Production> findByWorkOrderId(Long workOrderId);

    Optional<Production> findFirstByWorkOrderIdOrderByIdDesc(Long workOrderId);
}