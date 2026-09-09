package com.chandra.production.service;

import com.chandra.production.dto.DispatchRequest;
import com.chandra.production.entity.Dispatch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DispatchService {

    Dispatch createDispatch(DispatchRequest request);

    Dispatch getDispatchById(Long id);

    List<Dispatch> getAllDispatches();

    List<Dispatch> getDispatchesByProductionId(Long productionId);

    List<Dispatch> getDispatchesByCustomerName(String customerName);

    List<Dispatch> getDispatchesByStatus(String status);

    Page<Dispatch> getAllDispatches(Pageable pageable);

    Dispatch markAsReady(Long id);

    Dispatch dispatch(Long id);

    Dispatch cancelDispatch(Long id);
}