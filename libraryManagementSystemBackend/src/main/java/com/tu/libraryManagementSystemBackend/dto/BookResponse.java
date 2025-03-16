package com.tu.libraryManagementSystemBackend.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BookResponse(
        UUID id,
        String title,
        String isbn,
        String author,
        String status,
        String genre,
        int quantity,
        BigDecimal price,
        String imageUrl
) {}