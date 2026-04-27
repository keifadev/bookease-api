package com.keifa.bookease.user.dto.response;

public record UserUpdateResponseDto(
        String name,
        String email,
        String phone
) {
}
