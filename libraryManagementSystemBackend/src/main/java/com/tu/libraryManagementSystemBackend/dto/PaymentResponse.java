package com.tu.libraryManagementSystemBackend.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentResponse(UUID id, BigDecimal amount, String paymentMethod, String transactionId, String status) {
}
