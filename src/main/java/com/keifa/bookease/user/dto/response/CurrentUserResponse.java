package com.keifa.bookease.user.dto.response;

import com.keifa.bookease.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record CurrentUserResponse(
        UUID id,
        String name,
        String email,
        String phone,
        Role role,
        LocalDateTime createdAt
) {
}
