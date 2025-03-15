package com.tu.libraryManagementSystemBackend.dto;

import java.util.UUID;

public record LoginResponse(
        String token,
        UUID userId,
        String role
) {}
