package com.example.cinema.cinemaws.dto;

import lombok.Builder;

@Builder
public record OtpVerifyTO(String email, String name, TransactionTypeEn trxType, String otp) {
}
