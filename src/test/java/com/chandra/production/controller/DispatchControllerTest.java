package com.chandra.production.controller;

import com.chandra.production.dto.DispatchRequest;
import com.chandra.production.entity.Dispatch;
import com.chandra.production.entity.DispatchStatus;
import com.chandra.production.service.DispatchService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DispatchControllerTest {

    @Mock
    private DispatchService dispatchService;

    @InjectMocks
    private DispatchController dispatchController;

    private Dispatch dispatch;

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
    }

    // ====================================================
    // TEST 1: Get Dispatch By ID
    // ====================================================

    @Test
    void getDispatchById_shouldReturnDispatch() {

        when(dispatchService.getDispatchById(1L))
                .thenReturn(dispatch);

        ResponseEntity<Dispatch> response =
                dispatchController.getDispatchById(1L);

        assertEquals(
                200,
                response.getStatusCode().value());

        assertNotNull(response.getBody());

        assertEquals(
                1L,
                response.getBody().getId());

        assertEquals(
                "ABC Customer",
                response.getBody().getCustomerName());

        verify(dispatchService)
                .getDispatchById(1L);
    }

    // ====================================================
    // TEST 2: Get All Dispatches
    // ====================================================

    @Test
    void getAllDispatches_shouldReturnDispatchList() {

        Dispatch dispatch2 = Dispatch.builder()
                .id(2L)
                .dispatchQuantity(20.0)
                .customerName("XYZ Customer")
                .destination("Mumbai")
                .status(DispatchStatus.READY)
                .build();

        when(dispatchService.getAllDispatches())
                .thenReturn(List.of(dispatch, dispatch2));

        ResponseEntity<List<Dispatch>> response =
                dispatchController.getAllDispatches();

        assertEquals(
                200,
                response.getStatusCode().value());

        assertNotNull(response.getBody());

        assertEquals(
                2,
                response.getBody().size());

        verify(dispatchService)
                .getAllDispatches();
    }

    // ====================================================
    // TEST 3: Create Dispatch
    // ====================================================

    @Test
    void createDispatch_shouldCreateDispatch() {

        DispatchRequest request = new DispatchRequest();

        request.setProductionId(1L);
        request.setDispatchQuantity(10.0);
        request.setCustomerName("ABC Customer");
        request.setDestination("Delhi");
        request.setRemarks("Test dispatch");

        when(dispatchService.createDispatch(request))
                .thenReturn(dispatch);

        ResponseEntity<Dispatch> response =
                dispatchController.createDispatch(request);

        assertEquals(
                201,
                response.getStatusCode().value());

        assertNotNull(response.getBody());

        assertEquals(
                1L,
                response.getBody().getId());

        assertEquals(
                "ABC Customer",
                response.getBody().getCustomerName());

        verify(dispatchService)
                .createDispatch(request);
    }

    // ====================================================
    // TEST 4: Get Dispatches By Production ID
    // ====================================================

    @Test
    void getDispatchesByProductionId_shouldReturnDispatches() {

        when(dispatchService
                .getDispatchesByProductionId(1L))
                .thenReturn(List.of(dispatch));

        ResponseEntity<List<Dispatch>> response =
                dispatchController
                        .getDispatchesByProductionId(1L);

        assertEquals(
                200,
                response.getStatusCode().value());

        assertNotNull(response.getBody());

        assertEquals(
                1,
                response.getBody().size());

        assertEquals(
                1L,
                response.getBody()
                        .get(0)
                        .getId());

        verify(dispatchService)
                .getDispatchesByProductionId(1L);
    }

    // ====================================================
    // TEST 5: Mark Dispatch As Ready
    // ====================================================

    @Test
    void markAsReady_shouldReturnReadyDispatch() {

        dispatch.setStatus(
                DispatchStatus.READY);

        when(dispatchService.markAsReady(1L))
                .thenReturn(dispatch);

        ResponseEntity<Dispatch> response =
                dispatchController.markAsReady(1L);

        assertEquals(
                200,
                response.getStatusCode().value());

        assertNotNull(response.getBody());

        assertEquals(
                DispatchStatus.READY,
                response.getBody().getStatus());

        verify(dispatchService)
                .markAsReady(1L);
    }

    // ====================================================
    // TEST 6: Dispatch
    // ====================================================

    @Test
    void dispatch_shouldReturnDispatchedDispatch() {

        dispatch.setStatus(
                DispatchStatus.DISPATCHED);

        when(dispatchService.dispatch(1L))
                .thenReturn(dispatch);

        ResponseEntity<Dispatch> response =
                dispatchController.dispatch(1L);

        assertEquals(
                200,
                response.getStatusCode().value());

        assertNotNull(response.getBody());

        assertEquals(
                DispatchStatus.DISPATCHED,
                response.getBody().getStatus());

        verify(dispatchService)
                .dispatch(1L);
    }

    // ====================================================
    // TEST 7: Cancel Dispatch
    // ====================================================

    @Test
    void cancelDispatch_shouldReturnCancelledDispatch() {

        dispatch.setStatus(
                DispatchStatus.CANCELLED);

        when(dispatchService.cancelDispatch(1L))
                .thenReturn(dispatch);

        ResponseEntity<Dispatch> response =
                dispatchController.cancelDispatch(1L);

        assertEquals(
                200,
                response.getStatusCode().value());

        assertNotNull(response.getBody());

        assertEquals(
                DispatchStatus.CANCELLED,
                response.getBody().getStatus());

        verify(dispatchService)
                .cancelDispatch(1L);
    }
}