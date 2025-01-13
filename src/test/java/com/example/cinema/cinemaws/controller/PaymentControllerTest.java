package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.model.Payment;
import com.example.cinema.cinemaws.model.Reservation;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.PaymentService;
import com.example.cinema.cinemaws.service.StripeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentService paymentService;

    @Mock
    private ApiResponseFactory apiResponseFactory;

    @Mock
    private StripeService stripeService;

    private Payment payment;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        payment = new Payment();
        payment.setPaymentId(1L);
        payment.setReservation(new Reservation());
        payment.setPaymentMethod("Credit Card");
    }

    @Test
    public void givenPaymentId_whenGetPaymentById_thenReturnPayment() {
        // Given
        when(paymentService.getPaymentById(1L)).thenReturn(payment);
        ApiResponseTO<Payment> expectedApiResponse = ApiResponseTO.<Payment>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(payment)
                .build();
        when(apiResponseFactory.createResponse(eq(ResponseCodeEn.SUCCESS_OPERATION), any(Payment.class)))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Payment>> response = paymentController.getPaymentById(1L);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenPaginationParams_whenGetAllPayments_thenReturnPaymentPage() {
        // Given
        Page<Payment> paymentPage = new PageImpl<>(Collections.singletonList(payment));
        when(paymentService.getAllPayments(1, 10, "paymentId", "desc", null, null)).thenReturn(paymentPage);
        ApiResponseTO<Page> expectedApiResponse = ApiResponseTO.<Page>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(paymentPage)
                .build();
        when(apiResponseFactory.createResponse(eq(ResponseCodeEn.SUCCESS_OPERATION), any(Page.class)))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Page<Payment>>> response = paymentController.getAllPayments(1, 10, "paymentId", "desc", null, null);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenPayment_whenCreatePayment_thenReturnCreatedPayment() {
        // Given
        when(paymentService.createPayment(any(Payment.class))).thenReturn(payment);
        ApiResponseTO<Payment> expectedApiResponse = ApiResponseTO.<Payment>builder()
                .code(ResponseCodeEn.SUCCESS_CREATED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_CREATED.getCode())
                .message("success.default")
                .data(payment)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_CREATED, payment))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Payment>> response = paymentController.createPayment(payment);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenPaymentIdAndDetails_whenUpdatePayment_thenReturnUpdatedPayment() {
        // Given
        when(paymentService.updatePayment(eq(1L), any(Payment.class))).thenReturn(payment);
        ApiResponseTO<Payment> expectedApiResponse = ApiResponseTO.<Payment>builder()
                .code(ResponseCodeEn.SUCCESS_UPDATED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_UPDATED.getCode())
                .message("success.default")
                .data(payment)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_UPDATED, payment))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Payment>> response = paymentController.updatePayment(1L, payment);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenPaymentId_whenDeletePayment_thenReturnNoContent() {
        // Given
        doNothing().when(paymentService).deletePayment(1L);
        ApiResponseTO<Object> expectedApiResponse = ApiResponseTO.<Object>builder()
                .code(ResponseCodeEn.SUCCESS_DELETED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_DELETED.getCode())
                .message("success.default")
                .data(null)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_DELETED))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Object>> response = paymentController.deletePayment(1L);

        // Then
        assertEquals(ResponseEntity.ok().body(expectedApiResponse), response);
        verify(paymentService, times(1)).deletePayment(1L);
    }
}
