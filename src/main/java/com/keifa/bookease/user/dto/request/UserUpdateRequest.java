package com.keifa.bookease.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
        String name,

        @Email(message = "O e-mail informado é inválido.")
        String email,

        @Pattern(
                regexp = "^(?:\\+55\\s?)?(?:\\(?\\d{2}\\)?\\s?)(?:9\\d{4}|\\d{4})-?\\d{4}$",
                message = "O telefone deve estar em um formato brasileiro válido, como: (11) 91234-5678 ou (11) 1234-5678."
        )
        String phone
)
{
}