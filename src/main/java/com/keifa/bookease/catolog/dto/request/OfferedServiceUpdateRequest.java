package com.keifa.bookease.catolog.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record OfferedServiceUpdateRequest(
        @Size(min = 5, max = 100, message = "O nome deve ter entre 5 e 100 caracteres.")
        String name,

        @Size(min = 20, max = 300, message = "A descrição deve ter entre 20 e 300 caracteres.")
        String description,

        BigDecimal price,

        @Min(value = 15, message = "A duração deve ser de no mínimo 15 minutos.")
        Integer durationMinutes
) {
}
