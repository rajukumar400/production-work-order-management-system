
        package com.chandra.production.service.impl;

import com.chandra.production.dto.DispatchRequest;
import com.chandra.production.entity.Dispatch;
import com.chandra.production.entity.DispatchStatus;
import com.chandra.production.entity.Production;
import com.chandra.production.entity.QualityCheck;
import com.chandra.production.entity.QualityStatus;
import com.chandra.production.repository.DispatchRepository;
import com.chandra.production.repository.ProductionRepository;
import com.chandra.production.repository.QualityCheckRepository;
import com.chandra.production.service.DispatchService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DispatchServiceImpl implements DispatchService {

    private final DispatchRepository dispatchRepository;
    private final ProductionRepository productionRepository;
    private final QualityCheckRepository qualityCheckRepository;

    // =========================================================
    // CREATE DISPATCH
    // =========================================================

    @Override
    @Transactional
    public Dispatch createDispatch(DispatchRequest request) {

        Production production = productionRepository
                .findById(request.getProductionId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Production not found with id: "
                                        + request.getProductionId()));

        // -----------------------------------------------------
        // 1. Production must be COMPLETED
        // -----------------------------------------------------

        if (production.getStatus() == null ||
                !production.getStatus().name().equals("COMPLETED")) {

            throw new RuntimeException(
                    "Only COMPLETED production can be dispatched");
        }

        Double dispatchQuantity =
                request.getDispatchQuantity();

        // -----------------------------------------------------
        // 2. Current dispatch cannot exceed actual production
        // -----------------------------------------------------

        if (dispatchQuantity >
                production.getActualQuantity()) {

            throw new RuntimeException(
                    "Dispatch quantity cannot be greater than actual production quantity");
        }

        // -----------------------------------------------------
        // 3. Check quality inspection
        // -----------------------------------------------------

        List<QualityCheck> qualityChecks =
                qualityCheckRepository
                        .findByProductionId(production.getId());

        if (qualityChecks.isEmpty()) {

            throw new RuntimeException(
                    "Quality check is required before dispatch");
        }

        // -----------------------------------------------------
        // 4. Calculate quality approved quantity
        // -----------------------------------------------------

        double approvedQuantity =
                qualityChecks.stream()
                        .filter(qc ->
                                qc.getStatus() == QualityStatus.PASSED ||
                                        qc.getStatus() == QualityStatus.PARTIALLY_PASSED)
                        .mapToDouble(
                                QualityCheck::getPassedQuantity)
                        .sum();

        // -----------------------------------------------------
        // 5. Dispatch cannot exceed quality approved quantity
        // -----------------------------------------------------

        if (dispatchQuantity > approvedQuantity) {

            throw new RuntimeException(
                    "Dispatch quantity cannot be greater than quality approved quantity");
        }

        // -----------------------------------------------------
        // 6. Calculate already dispatched quantity
        // -----------------------------------------------------

        Double totalDispatchedQuantity =
                dispatchRepository
                        .getTotalDispatchedQuantity(
                                production.getId());

        if (totalDispatchedQuantity == null) {
            totalDispatchedQuantity = 0.0;
        }

        // -----------------------------------------------------
        // 7. Prevent over-dispatch
        // -----------------------------------------------------

        double totalAfterDispatch =
                totalDispatchedQuantity + dispatchQuantity;

        if (totalAfterDispatch >
                production.getActualQuantity()) {

            throw new RuntimeException(
                    "Total dispatch quantity cannot be greater than actual production quantity. "
                            + "Already dispatched: "
                            + totalDispatchedQuantity
                            + ", requested: "
                            + dispatchQuantity
                            + ", actual production: "
                            + production.getActualQuantity());
        }

        // -----------------------------------------------------
        // 8. Create dispatch
        // -----------------------------------------------------

        Dispatch dispatch = Dispatch.builder()
                .production(production)
                .dispatchQuantity(dispatchQuantity)
                .customerName(request.getCustomerName())
                .destination(request.getDestination())
                .remarks(request.getRemarks())
                .status(DispatchStatus.PENDING)
                .build();

        return dispatchRepository.save(dispatch);
    }

    // =========================================================
    // GET DISPATCH BY ID
    // =========================================================

    @Override
    public Dispatch getDispatchById(Long id) {

        return dispatchRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Dispatch not found with id: "
                                        + id));
    }

    // =========================================================
    // GET ALL DISPATCHES
    // =========================================================

    @Override
    public List<Dispatch> getAllDispatches() {

        return dispatchRepository.findAll();
    }

    // =========================================================
    // GET DISPATCHES BY PRODUCTION ID
    // =========================================================

    @Override
    public List<Dispatch> getDispatchesByProductionId(
            Long productionId) {

        return dispatchRepository
                .findByProductionId(productionId);
    }

    // =========================================================
    // SEARCH BY CUSTOMER NAME
    // =========================================================

    @Override
    public List<Dispatch> getDispatchesByCustomerName(
            String customerName) {

        return dispatchRepository
                .findByCustomerNameContainingIgnoreCase(
                        customerName);
    }

    // =========================================================
    // FILTER BY STATUS
    // =========================================================

    @Override
    public List<Dispatch> getDispatchesByStatus(
            String status) {

        DispatchStatus dispatchStatus;

        try {

            dispatchStatus =
                    DispatchStatus.valueOf(
                            status.toUpperCase());

        } catch (IllegalArgumentException e) {

            throw new RuntimeException(
                    "Invalid dispatch status: " + status);
        }

        return dispatchRepository
                .findByStatus(dispatchStatus);
    }

    // =========================================================
    // PAGINATION
    // =========================================================

    @Override
    public Page<Dispatch> getAllDispatches(
            Pageable pageable) {

        return dispatchRepository.findAll(pageable);
    }

    // =========================================================
    // MARK AS READY
    // =========================================================

    @Override
    @Transactional
    public Dispatch markAsReady(Long id) {

        Dispatch dispatch =
                getDispatchById(id);

        if (dispatch.getStatus() !=
                DispatchStatus.PENDING) {

            throw new RuntimeException(
                    "Only PENDING dispatch can be marked as READY");
        }

        dispatch.setStatus(
                DispatchStatus.READY);

        return dispatchRepository.save(dispatch);
    }

    // =========================================================
    // DISPATCH
    // =========================================================

    @Override
    @Transactional
    public Dispatch dispatch(Long id) {

        Dispatch dispatch =
                getDispatchById(id);

        if (dispatch.getStatus() !=
                DispatchStatus.READY) {

            throw new RuntimeException(
                    "Only READY dispatch can be dispatched");
        }

        dispatch.setStatus(
                DispatchStatus.DISPATCHED);

        dispatch.setDispatchDate(
                LocalDateTime.now());

        return dispatchRepository.save(dispatch);
    }

    // =========================================================
    // CANCEL DISPATCH
    // =========================================================

    @Override
    @Transactional
    public Dispatch cancelDispatch(Long id) {

        Dispatch dispatch =
                getDispatchById(id);

        if (dispatch.getStatus() ==
                DispatchStatus.DISPATCHED) {

            throw new RuntimeException(
                    "Dispatched order cannot be cancelled");
        }

        dispatch.setStatus(
                DispatchStatus.CANCELLED);

        return dispatchRepository.save(dispatch);
    }
}
