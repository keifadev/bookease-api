package com.keifa.bookease.user.dto.response;

import com.keifa.bookease.enums.Role;

import java.util.UUID;

public record UserPublicResponseDto(
        UUID id,
        String name,
        Role role
) {
}
