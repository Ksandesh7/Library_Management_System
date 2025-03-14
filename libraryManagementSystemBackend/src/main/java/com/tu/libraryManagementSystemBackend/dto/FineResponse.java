package com.tu.libraryManagementSystemBackend.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record FineResponse(UUID id, BigDecimal amount, String status) {
}
