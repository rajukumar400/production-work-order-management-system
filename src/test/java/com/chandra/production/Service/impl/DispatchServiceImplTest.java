package com.chandra.production.Service.impl;

import com.chandra.production.dto.DispatchRequest;
import com.chandra.production.entity.Dispatch;
import com.chandra.production.entity.DispatchStatus;
import com.chandra.production.entity.Production;
import com.chandra.production.entity.ProductionStatus;
import com.chandra.production.entity.QualityCheck;
import com.chandra.production.entity.QualityStatus;
import com.chandra.production.repository.DispatchRepository;
import com.chandra.production.repository.ProductionRepository;
import com.chandra.production.repository.QualityCheckRepository;

import com.chandra.production.service.impl.DispatchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DispatchServiceImplTest {

    @Mock
    private DispatchRepository dispatchRepository;

    @Mock
    private ProductionRepository productionRepository;

    @Mock
    private QualityCheckRepository qualityCheckRepository;

    @InjectMocks
    private DispatchServiceImpl dispatchService;

    private Dispatch dispatch;
    private Production production;
    private QualityCheck qualityCheck;
    private DispatchRequest dispatchRequest;

    @BeforeEach
    void setUp() {

        dispatch = Dispatch.builder()
                .id(1L)
                .dispatchQuantity(10.0)
                .customerName("ABC Customer")
                .destination("Delhi")
                .remarks("Test dispatch")
                .status(DispatchStatus.PENDING)
                .build();

        production = Production.builder()
                .id(1L)
                .productionQuantity(100.0)
                .actualQuantity(80.0)
                .status(ProductionStatus.COMPLETED)
                .productionDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        qualityCheck = QualityCheck.builder()
                .id(1L)
                .production(production)
                .checkedQuantity(80.0)
                .passedQuantity(70.0)
                .failedQuantity(10.0)
                .status(QualityStatus.PASSED)
                .checkDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        dispatchRequest = new DispatchRequest();
        dispatchRequest.setProductionId(1L);
        dispatchRequest.setDispatchQuantity(50.0);
        dispatchRequest.setCustomerName("ABC Customer");
        dispatchRequest.setDestination("Delhi");
        dispatchRequest.setRemarks("Test dispatch");
    }

    // ====================================================
    // TEST 1: Get Dispatch By ID - Success
    // ====================================================

    @Test
    void getDispatchById_shouldReturnDispatch() {

        when(dispatchRepository.findById(1L))
                .thenReturn(Optional.of(dispatch));

        Dispatch result =
                dispatchService.getDispatchById(1L);

        assertNotNull(result);

        assertEquals(1L, result.getId());

        assertEquals(
                10.0,
                result.getDispatchQuantity());

        assertEquals(
                "ABC Customer",
                result.getCustomerName());

        verify(dispatchRepository)
                .findById(1L);
    }

    // ====================================================
    // TEST 2: Get Dispatch By ID - Not Found
    // ====================================================

    @Test
    void getDispatchById_shouldThrowExceptionWhenNotFound() {

        when(dispatchRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> dispatchService
                                .getDispatchById(1L));

        assertEquals(
                "Dispatch not found with id: 1",
                exception.getMessage());

        verify(dispatchRepository)
                .findById(1L);
    }

    // ====================================================
    // TEST 3: Mark As Ready - Success
    // ====================================================

    @Test
    void markAsReady_shouldChangeStatusToReady() {

        dispatch.setStatus(
                DispatchStatus.PENDING);

        when(dispatchRepository.findById(1L))
                .thenReturn(Optional.of(dispatch));

        when(dispatchRepository.save(any(Dispatch.class)))
                .thenReturn(dispatch);

        Dispatch result =
                dispatchService.markAsReady(1L);

        assertNotNull(result);

        assertEquals(
                DispatchStatus.READY,
                result.getStatus());

        verify(dispatchRepository)
                .findById(1L);

        verify(dispatchRepository)
                .save(dispatch);
    }

    // ====================================================
    // TEST 4: Mark As Ready - Invalid Status
    // ====================================================

    @Test
    void markAsReady_shouldThrowExceptionWhenDispatchIsNotPending() {

        dispatch.setStatus(
                DispatchStatus.DISPATCHED);

        when(dispatchRepository.findById(1L))
                .thenReturn(Optional.of(dispatch));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> dispatchService
                                .markAsReady(1L));

        assertEquals(
                "Only PENDING dispatch can be marked as READY",
                exception.getMessage());

        verify(dispatchRepository)
                .findById(1L);

        verify(dispatchRepository, never())
                .save(any(Dispatch.class));
    }

    // ====================================================
    // TEST 5: Dispatch - Success
    // ====================================================

    @Test
    void dispatch_shouldChangeStatusToDispatched() {

        dispatch.setStatus(
                DispatchStatus.READY);

        when(dispatchRepository.findById(1L))
                .thenReturn(Optional.of(dispatch));

        when(dispatchRepository.save(any(Dispatch.class)))
                .thenReturn(dispatch);

        Dispatch result =
                dispatchService.dispatch(1L);

        assertNotNull(result);

        assertEquals(
                DispatchStatus.DISPATCHED,
                result.getStatus());

        assertNotNull(
                result.getDispatchDate());

        verify(dispatchRepository)
                .findById(1L);

        verify(dispatchRepository)
                .save(dispatch);
    }

    // ====================================================
    // TEST 6: Dispatch - Invalid Status
    // ====================================================

    @Test
    void dispatch_shouldThrowExceptionWhenDispatchIsNotReady() {

        dispatch.setStatus(
                DispatchStatus.PENDING);

        when(dispatchRepository.findById(1L))
                .thenReturn(Optional.of(dispatch));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> dispatchService
                                .dispatch(1L));

        assertEquals(
                "Only READY dispatch can be dispatched",
                exception.getMessage());

        verify(dispatchRepository)
                .findById(1L);

        verify(dispatchRepository, never())
                .save(any(Dispatch.class));
    }

    // ====================================================
    // TEST 7: Cancel Dispatch - Success
    // ====================================================

    @Test
    void cancelDispatch_shouldChangeStatusToCancelled() {

        dispatch.setStatus(
                DispatchStatus.PENDING);

        when(dispatchRepository.findById(1L))
                .thenReturn(Optional.of(dispatch));

        when(dispatchRepository.save(any(Dispatch.class)))
                .thenReturn(dispatch);

        Dispatch result =
                dispatchService.cancelDispatch(1L);

        assertNotNull(result);

        assertEquals(
                DispatchStatus.CANCELLED,
                result.getStatus());

        verify(dispatchRepository)
                .findById(1L);

        verify(dispatchRepository)
                .save(dispatch);
    }

    // ====================================================
    // TEST 8: Cancel Dispatch - Already Dispatched
    // ====================================================

    @Test
    void cancelDispatch_shouldThrowExceptionWhenAlreadyDispatched() {

        dispatch.setStatus(
                DispatchStatus.DISPATCHED);

        when(dispatchRepository.findById(1L))
                .thenReturn(Optional.of(dispatch));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> dispatchService
                                .cancelDispatch(1L));

        assertEquals(
                "Dispatched order cannot be cancelled",
                exception.getMessage());

        verify(dispatchRepository)
                .findById(1L);

        verify(dispatchRepository, never())
                .save(any(Dispatch.class));
    }

    // ====================================================
    // TEST 9: Create Dispatch - Success
    // ====================================================

    @Test
    void createDispatch_shouldCreateDispatchSuccessfully() {

        when(productionRepository.findById(1L))
                .thenReturn(Optional.of(production));

        when(qualityCheckRepository
                .findByProductionId(1L))
                .thenReturn(List.of(qualityCheck));

        when(dispatchRepository.save(any(Dispatch.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        Dispatch result =
                dispatchService
                        .createDispatch(dispatchRequest);

        assertNotNull(result);

        assertEquals(
                50.0,
                result.getDispatchQuantity());

        assertEquals(
                "ABC Customer",
                result.getCustomerName());

        assertEquals(
                "Delhi",
                result.getDestination());

        assertEquals(
                DispatchStatus.PENDING,
                result.getStatus());

        assertEquals(
                production,
                result.getProduction());

        verify(productionRepository)
                .findById(1L);

        verify(qualityCheckRepository)
                .findByProductionId(1L);

        verify(dispatchRepository)
                .save(any(Dispatch.class));
    }

    // ====================================================
    // TEST 10: Create Dispatch - Production Not Found
    // ====================================================

    @Test
    void createDispatch_shouldThrowExceptionWhenProductionNotFound() {

        when(productionRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> dispatchService
                                .createDispatch(dispatchRequest));

        assertEquals(
                "Production not found with id: 1",
                exception.getMessage());

        verify(productionRepository)
                .findById(1L);

        verifyNoInteractions(
                qualityCheckRepository);

        verify(dispatchRepository, never())
                .save(any(Dispatch.class));
    }

    // ====================================================
    // TEST 11: Create Dispatch - Production Not Completed
    // ====================================================

    @Test
    void createDispatch_shouldThrowExceptionWhenProductionNotCompleted() {

        production.setStatus(
                ProductionStatus.PLANNED);

        when(productionRepository.findById(1L))
                .thenReturn(Optional.of(production));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> dispatchService
                                .createDispatch(dispatchRequest));

        assertEquals(
                "Only COMPLETED production can be dispatched",
                exception.getMessage());

        verify(productionRepository)
                .findById(1L);

        verifyNoInteractions(
                qualityCheckRepository);

        verify(dispatchRepository, never())
                .save(any(Dispatch.class));
    }

    // ====================================================
    // TEST 12: Create Dispatch - Quantity Greater Than
    // Actual Production Quantity
    // ====================================================

    @Test
    void createDispatch_shouldThrowExceptionWhenDispatchQuantityIsGreaterThanActualQuantity() {

        dispatchRequest.setDispatchQuantity(100.0);

        when(productionRepository.findById(1L))
                .thenReturn(Optional.of(production));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> dispatchService
                                .createDispatch(dispatchRequest));

        assertEquals(
                "Dispatch quantity cannot be greater than actual production quantity",
                exception.getMessage());

        verify(productionRepository)
                .findById(1L);

        verifyNoInteractions(
                qualityCheckRepository);

        verify(dispatchRepository, never())
                .save(any(Dispatch.class));
    }

    // ====================================================
    // TEST 13: Create Dispatch - No Quality Check
    // ====================================================

    @Test
    void createDispatch_shouldThrowExceptionWhenQualityCheckIsMissing() {

        when(productionRepository.findById(1L))
                .thenReturn(Optional.of(production));

        when(qualityCheckRepository
                .findByProductionId(1L))
                .thenReturn(List.of());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> dispatchService
                                .createDispatch(dispatchRequest));

        assertEquals(
                "Quality check is required before dispatch",
                exception.getMessage());

        verify(productionRepository)
                .findById(1L);

        verify(qualityCheckRepository)
                .findByProductionId(1L);

        verify(dispatchRepository, never())
                .save(any(Dispatch.class));
    }

    // ====================================================
    // TEST 14: Create Dispatch - Approved Quantity Insufficient
    // ====================================================

    @Test
    void createDispatch_shouldThrowExceptionWhenApprovedQuantityIsInsufficient() {

        dispatchRequest.setDispatchQuantity(75.0);

        when(productionRepository.findById(1L))
                .thenReturn(Optional.of(production));

        when(qualityCheckRepository
                .findByProductionId(1L))
                .thenReturn(List.of(qualityCheck));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> dispatchService
                                .createDispatch(dispatchRequest));

        assertEquals(
                "Dispatch quantity cannot be greater than quality approved quantity",
                exception.getMessage());

        verify(productionRepository)
                .findById(1L);

        verify(qualityCheckRepository)
                .findByProductionId(1L);

        verify(dispatchRepository, never())
                .save(any(Dispatch.class));
    }
}