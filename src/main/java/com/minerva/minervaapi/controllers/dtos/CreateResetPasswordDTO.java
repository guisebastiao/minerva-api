package com.minerva.minervaapi.controllers.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateResetPasswordDTO(

        @NotBlank(message = "Informe seu email")
        @Email(message = "Informe um email válido")
        @Size(max = 250, message = "O email deve ter menos de 250 caracteres")
        String email
) { }
