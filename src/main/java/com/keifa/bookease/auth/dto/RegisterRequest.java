package com.keifa.bookease.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
        String name,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O e-mail informado é inválido.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&()_+\\-=\\[\\]{};:'\",.<>/\\\\|]).{8,}$",
                message = "A nova senha deve ter no mínimo 8 caracteres e conter pelo menos 1 letra maiúscula, 1 letra minúscula, 1 número e 1 caractere especial."
        )
        String password,

        @NotBlank(message = "O telefone é obrigatório.")
        @Pattern(
                regexp = "^(?:\\+55\\s?)?(?:\\(?\\d{2}\\)?\\s?)(?:9\\d{4}|\\d{4})-?\\d{4}$",
                message = "O telefone deve estar em um formato brasileiro válido, como: (11) 91234-5678 ou (11) 1234-5678."
        )
        String phone

) {
}
