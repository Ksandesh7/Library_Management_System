package com.tu.libraryManagementSystemBackend.dto;

import java.time.LocalDate;
import java.util.UUID;

public record LoanResponse(
        UUID id,
        UUID userId,
        UUID bookId,
        LocalDate checkoutDate,
        LocalDate dueDate,
        String status
) {}
