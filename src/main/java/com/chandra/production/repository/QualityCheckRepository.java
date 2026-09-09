package com.chandra.production.repository;


import com.chandra.production.entity.QualityCheck;
import com.chandra.production.entity.QualityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QualityCheckRepository
        extends JpaRepository<QualityCheck, Long> {

    List<QualityCheck> findByProductionId(Long productionId);

    List<QualityCheck> findByStatus(QualityStatus status);
}
