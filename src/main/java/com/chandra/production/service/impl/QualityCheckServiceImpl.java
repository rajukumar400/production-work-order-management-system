package com.chandra.production.service.impl;

import com.chandra.production.dto.QualityCheckRequest;
import com.chandra.production.entity.Production;
import com.chandra.production.entity.QualityCheck;
import com.chandra.production.entity.QualityStatus;
import com.chandra.production.repository.ProductionRepository;
import com.chandra.production.repository.QualityCheckRepository;
import com.chandra.production.service.QualityCheckService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QualityCheckServiceImpl implements QualityCheckService {

    private final QualityCheckRepository qualityCheckRepository;
    private final ProductionRepository productionRepository;

    @Override
    @Transactional
    public QualityCheck createQualityCheck(QualityCheckRequest request) {

        Production production = productionRepository
                .findById(request.getProductionId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Production not found with id: "
                                        + request.getProductionId()));

        if (production.getStatus().name().equals("CANCELLED")) {
            throw new RuntimeException(
                    "Quality check cannot be created for cancelled production");
        }

        Double checkedQuantity = request.getCheckedQuantity();

        Double passedQuantity =
                request.getPassedQuantity() != null
                        ? request.getPassedQuantity()
                        : 0.0;

        Double failedQuantity =
                request.getFailedQuantity() != null
                        ? request.getFailedQuantity()
                        : 0.0;

        if (passedQuantity + failedQuantity > checkedQuantity) {
            throw new RuntimeException(
                    "Passed quantity + failed quantity cannot be greater than checked quantity");
        }

        QualityCheck qualityCheck = QualityCheck.builder()
                .production(production)
                .checkedQuantity(checkedQuantity)
                .passedQuantity(passedQuantity)
                .failedQuantity(failedQuantity)
                .status(QualityStatus.PENDING)
                .remarks(request.getRemarks())
                .build();

        return qualityCheckRepository.save(qualityCheck);
    }

    @Override
    public QualityCheck getQualityCheckById(Long id) {

        return qualityCheckRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Quality check not found with id: " + id));
    }

    @Override
    public List<QualityCheck> getAllQualityChecks() {

        return qualityCheckRepository.findAll();
    }

    @Override
    public List<QualityCheck> getQualityChecksByProductionId(
            Long productionId) {

        return qualityCheckRepository
                .findByProductionId(productionId);
    }

    @Override
    @Transactional
    public QualityCheck passQualityCheck(Long id) {

        QualityCheck qualityCheck = getQualityCheckById(id);

        if (qualityCheck.getStatus() == null) {
            throw new RuntimeException(
                    "Quality check status is null");
        }

        if (qualityCheck.getStatus() != QualityStatus.PENDING) {
            throw new RuntimeException(
                    "Only PENDING quality checks can be passed");
        }

        if (qualityCheck.getFailedQuantity() != null
                && qualityCheck.getFailedQuantity() > 0) {

            qualityCheck.setStatus(
                    QualityStatus.PARTIALLY_PASSED);

        } else {

            qualityCheck.setStatus(
                    QualityStatus.PASSED);
        }

        return qualityCheckRepository.save(qualityCheck);
    }

    @Override
    @Transactional
    public QualityCheck failQualityCheck(Long id) {

        QualityCheck qualityCheck = getQualityCheckById(id);

        if (qualityCheck.getStatus() == null) {
            throw new RuntimeException(
                    "Quality check status is null");
        }

        if (qualityCheck.getStatus() != QualityStatus.PENDING) {
            throw new RuntimeException(
                    "Only PENDING quality checks can be failed");
        }

        qualityCheck.setStatus(QualityStatus.FAILED);

        return qualityCheckRepository.save(qualityCheck);
    }
}