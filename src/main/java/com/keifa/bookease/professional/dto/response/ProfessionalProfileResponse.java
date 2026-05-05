package com.keifa.bookease.professional.dto.response;

import com.keifa.bookease.professional.enums.Specialty;

import java.util.UUID;

public record ProfessionalProfileResponse(
        UUID id,
        String bio,
        Specialty specialty,
        Integer cancellationPolicyHours
) {
}
