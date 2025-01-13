package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.model.Payment;
import com.example.cinema.cinemaws.repository.PaymentRepository;
import com.example.cinema.cinemaws.repository.ReservationRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    public Payment createPayment(Payment payment) {
        reservationRepository.findById(payment.getReservation().getReservationId())
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));

        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Page<Payment> getAllPayments(int page, int size, String sortBy, String sortOrder, String paymentMethod, String paymentStatus) {
        int adjustedPage = page > 0 ? page - 1 : 0;
        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy);
        Pageable pageable = PageRequest.of(adjustedPage, size, sort);
        Specification<Payment> paymentSpecification = createPaymentSpesification(paymentMethod, paymentStatus);
        return paymentRepository.findAll(paymentSpecification, pageable);
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));
    }

    public Payment updatePayment(Long id, Payment paymentDetails) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));

        existingPayment.setPaymentMethod(paymentDetails.getPaymentMethod());
        existingPayment.setPaymentStatus(paymentDetails.getPaymentStatus());
        existingPayment.setReservation(paymentDetails.getReservation());

        return paymentRepository.save(existingPayment);
    }

    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
        paymentRepository.delete(payment);
    }

    private Specification<Payment> createPaymentSpesification(String paymentMethod, String paymentStatus) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> paymentPredicates = new ArrayList<>();

            if (paymentMethod != null && !paymentMethod.isEmpty()) {
                paymentPredicates.add(criteriaBuilder.equal(root.get("paymentMethod"), paymentMethod));
            }

            if (paymentStatus != null && !paymentStatus.isEmpty()) {
                paymentPredicates.add(criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus));
            }

            return criteriaBuilder.and(paymentPredicates.toArray(new Predicate[0]));
        });
    }
}
