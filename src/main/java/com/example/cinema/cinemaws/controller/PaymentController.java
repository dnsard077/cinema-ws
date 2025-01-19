package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.model.Payment;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.PaymentService;
import com.example.cinema.cinemaws.service.StripeService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final ApiResponseFactory apiResponseFactory;
    private final StripeService stripeService;

    @Value("${stripe.webhook.key}")
    private String webhookKey;

    @GetMapping
    public ResponseEntity<ApiResponseTO<Page<Payment>>> getAllPayments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "paymentId") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) String paymentStatus
    ) {
        Page<Payment> payments = paymentService.getAllPayments(page, size, sortBy, sortOrder, paymentMethod, paymentStatus);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Payment>> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, payment);
    }

    @PostMapping
    public ResponseEntity<ApiResponseTO<Payment>> createPayment(@RequestBody Payment payment) {
        Payment createdPayment = paymentService.createPayment(payment);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_CREATED, createdPayment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Payment>> updatePayment(@PathVariable Long id, @RequestBody Payment paymentDetails) {
        Payment updatedPayment = paymentService.updatePayment(id, paymentDetails);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_UPDATED, updatedPayment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Object>> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_DELETED);
    }

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponseTO<String>> createCheckoutSession(@RequestBody Map<String, Object> request) {
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, stripeService.createCheckoutSession(
                Long.valueOf(String.valueOf(request.get("amount"))),
                (String) request.getOrDefault("currency", "usd"),
                (String) request.get("productName")));
    }

    @PostMapping("/checkout/intent")
    public ResponseEntity<ApiResponseTO<Map<String, String>>> createCheckoutIntent(@RequestBody Map<String, Object> request) {
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, stripeService.createPaymentIntent(Long.valueOf(String.valueOf(request.get("amount"))), (String) request.getOrDefault("currency", "usd")));
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;
        String STRIPE_WEBHOOK_SECRET = webhookKey;

        try {
            event = Webhook.constructEvent(payload, sigHeader, STRIPE_WEBHOOK_SECRET);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Signature");
        }
        stripeService.processEvent(event);
        return ResponseEntity.ok("Webhook received and processed");
    }
}