package com.keifa.bookease.user.dto.response;

import com.keifa.bookease.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record CurrentUserResponseDto(
        UUID id,
        String name,
        String email,
        String phone,
        Role role,
        LocalDateTime createdAt
) {
}
