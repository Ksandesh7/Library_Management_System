package com.tu.libraryManagementSystemBackend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(@NotNull UUID fineId, @DecimalMin("0.01") BigDecimal amount, @NotNull String paymentMethod) {
}
