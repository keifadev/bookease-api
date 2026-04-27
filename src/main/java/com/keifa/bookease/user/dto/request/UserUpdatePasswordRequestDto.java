package com.keifa.bookease.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdatePasswordRequestDto(
        @NotBlank(message = "A senha atual é obrigatória.")
        String password,

        @NotBlank(message = "A nova senha é obrigatória.")
        @Size(min = 8, message = "A nova senha deve ter no mínimo 8 caracteres.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&()_+\\-=\\[\\]{};:'\",.<>/\\\\|]).{8,}$",
                message = "A nova senha deve ter no mínimo 8 caracteres e conter pelo menos 1 letra maiúscula, 1 letra minúscula, 1 número e 1 caractere especial."
        )
        String newPassword,

        @NotBlank(message = "A confirmação da nova senha é obrigatória.")
        String confirmNewPassword
) {
}
