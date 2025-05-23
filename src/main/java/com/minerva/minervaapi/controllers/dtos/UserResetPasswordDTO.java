package com.minerva.minervaapi.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserResetPasswordDTO(
        @NotBlank(message = "Informe sua senha atual")
        @Size(min = 6, message = "A senha deve ter mais de 6 caracteres")
        @Size(max = 20, message = "A senha deve ter menos de 20 caracteres")
        @Pattern.List({
                @Pattern(regexp = ".*[A-Z].*", message = "A senha deve ter uma letra maiúscula"),
                @Pattern(regexp = ".*\\d.*\\d.*", message = "A senha deve ter dois números"),
                @Pattern(regexp = ".*[@$!%*?&.#].*", message = "A senha deve ter um caractere especial")
        })
        String currentPassword,

        @NotBlank(message = "Informe sua nova senha")
        @Size(min = 6, message = "A senha deve ter mais de 6 caracteres")
        @Size(max = 20, message = "A senha deve ter menos de 20 caracteres")
        @Pattern.List({
                @Pattern(regexp = ".*[A-Z].*", message = "A senha deve ter uma letra maiúscula"),
                @Pattern(regexp = ".*\\d.*\\d.*", message = "A senha deve ter dois números"),
                @Pattern(regexp = ".*[@$!%*?&.#].*", message = "A senha deve ter um caractere especial")
        })
        String newPassword,

        @NotBlank(message = "Confirme sua senha")
        @Size(min = 6, message = "A senha deve ter mais de 6 caracteres")
        @Size(max = 20, message = "A senha deve ter menos de 20 caracteres")
        @Pattern.List({
                @Pattern(regexp = ".*[A-Z].*", message = "A senha deve ter uma letra maiúscula"),
                @Pattern(regexp = ".*\\d.*\\d.*", message = "A senha deve ter dois números"),
                @Pattern(regexp = ".*[@$!%*?&.#].*", message = "A senha deve ter um caractere especial")
        })
        String confirmPassword
){ }
