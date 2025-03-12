package com.tu.libraryManagementSystemBackend.dto;

import java.util.UUID;

public record BookResponse(
        UUID id,
        String title,
        String isbn,
        String author,
        String status
) {}