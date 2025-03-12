package com.tu.libraryManagementSystemBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookRequest(
        @NotBlank String title,
        @NotBlank @Size(min=10, max=13) String isbn,
        @NotBlank String author
) {}