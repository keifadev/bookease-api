package com.keifa.bookease.catolog.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record OfferedServiceResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer durationMinutes
) {
}
