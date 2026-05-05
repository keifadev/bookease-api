package com.keifa.bookease.professional.dto.request;

import com.keifa.bookease.professional.enums.Specialty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProfessionalProfileRequest(
        @NotBlank(message = "A bio não pode estar em branco.")
        @Size(min = 50, max = 300, message = "A bio deve ter entre 50 e 300 caracteres.")
        String bio,

        @NotNull(message = "A especialidade não pode ser nula.")
        Specialty specialty,

        @Min(value = 2, message = "A política de cancelamento deve ser de no mínimo 2 horas")
        Integer cancellationPolicyHours
) {
}