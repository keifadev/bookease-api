package com.keifa.bookease.catolog.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record OfferedServiceRequest(
        @NotBlank(message = "O nome não pode estar em branco.")
        @Size(min = 5, max = 100, message = "O nome deve ter entre 5 e 100 caracteres.")
        String name,

        @NotBlank(message = "A descrição não pode estar em branco.")
        @Size(min = 20, max = 300, message = "A descrição deve ter entre 20 e 300 caracteres.")
        String description,

        @NotNull(message = "O preço não pode ser nulo.")
        BigDecimal price,

        @NotNull(message = "A duração do serviço em minutos não pode ser nula.")
        @Min(value = 15, message = "A duração deve ser de no mínimo 15 minutos.")
        Integer durationMinutes
) {
}
