
package com.chandra.production.service;

import com.chandra.production.dto.ProductionRequest;
import com.chandra.production.entity.Production;

import java.util.List;

public interface ProductionService {

    Production createProduction(ProductionRequest request);

    Production getProductionById(Long id);

    List<Production> getAllProductions();

    List<Production> getProductionsByWorkOrderId(Long workOrderId);

    Production startProduction(Long id);

    Production completeProduction(Long id);

    Production cancelProduction(Long id);
}

