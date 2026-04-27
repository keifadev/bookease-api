package com.keifa.bookease.user.dto.response;

import com.keifa.bookease.enums.Role;

import java.util.UUID;

public record AdminUserViewDTO(
        UUID userId,
        String name,
        String email,
        String phone,
        Role role,
        boolean active
) {
}
