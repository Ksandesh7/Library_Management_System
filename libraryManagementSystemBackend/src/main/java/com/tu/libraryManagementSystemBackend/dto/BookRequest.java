package com.tu.libraryManagementSystemBackend.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record BookRequest(
        @NotBlank String title,
        @NotBlank @Size(min=10, max=13) String isbn,
        @NotBlank String author,
        @NotBlank String genre,
        @Min(1) int quantity,
        @DecimalMin("0.0") BigDecimal price,
        String imageUrl
) {}