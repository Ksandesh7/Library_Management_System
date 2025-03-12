package com.tu.libraryManagementSystemBackend.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LoanRequest(
        @NotNull UUID userId,
        @NotNull UUID bookId
) {}
