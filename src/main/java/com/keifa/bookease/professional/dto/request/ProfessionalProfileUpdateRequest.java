package com.keifa.bookease.professional.dto.request;

import com.keifa.bookease.professional.enums.Specialty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ProfessionalProfileUpdateRequest(
        @Size(min = 25, max = 300, message = "A bio deve ter entre 25 e 300 caracteres.")
        String bio,

        Specialty specialty,

        @Min(value = 2, message = "A política de cancelamento deve ser de no mínimo 2 horas")
        Integer cancellationPolicyHours

) {
}
