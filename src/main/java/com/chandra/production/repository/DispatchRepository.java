package com.chandra.production.repository;

import com.chandra.production.entity.Dispatch;
import com.chandra.production.entity.DispatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DispatchRepository
        extends JpaRepository<Dispatch, Long> {

    List<Dispatch> findByProductionId(Long productionId);

    List<Dispatch> findByStatus(DispatchStatus status);

    List<Dispatch> findByCustomerNameContainingIgnoreCase(
            String customerName);

    @Query("SELECT COALESCE(SUM(d.dispatchQuantity), 0) " +
           "FROM Dispatch d " +
           "WHERE d.production.id = :productionId " +
           "AND d.status <> com.chandra.production.entity.DispatchStatus.CANCELLED")
    Double getTotalDispatchedQuantity(
            @Param("productionId") Long productionId);
}
