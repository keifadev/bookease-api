package com.keifa.bookease.catolog.dto.response;

import java.math.BigDecimal;

public record ProfessionalServiceResponse(
        String name,
        String description,
        BigDecimal price,
        Integer durationMinutes
) {
}
