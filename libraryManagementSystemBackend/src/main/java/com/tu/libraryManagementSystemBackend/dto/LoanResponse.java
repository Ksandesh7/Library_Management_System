package com.tu.libraryManagementSystemBackend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record LoanResponse(
        UUID id,
        UUID userId,
        UUID bookId,
        LocalDateTime checkoutDate,
        LocalDateTime dueDate,
        String status
) {}
