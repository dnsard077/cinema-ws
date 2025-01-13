package com.example.cinema.cinemaws.dto;

import lombok.Builder;

@Builder
public record OtpSendTO(String email, String name, TransactionTypeEn trxType) {
}
