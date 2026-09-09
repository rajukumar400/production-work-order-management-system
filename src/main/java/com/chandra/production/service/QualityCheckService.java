package com.chandra.production.service;


import com.chandra.production.dto.QualityCheckRequest;
import com.chandra.production.entity.QualityCheck;

import java.util.List;

public interface QualityCheckService {

    QualityCheck createQualityCheck(QualityCheckRequest request);

    QualityCheck getQualityCheckById(Long id);

    List<QualityCheck> getAllQualityChecks();

    List<QualityCheck> getQualityChecksByProductionId(Long productionId);

    QualityCheck passQualityCheck(Long id);

    QualityCheck failQualityCheck(Long id);
}
