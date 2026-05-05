package com.keifa.bookease.professional.dto.response;

import com.keifa.bookease.professional.enums.Specialty;

import java.time.LocalDateTime;

public record CurrentProfessionalProfileResponse(
        String bio,
        Specialty specialty,
        Integer cancellationPolicyHours,
        LocalDateTime createdAt
) {
}
