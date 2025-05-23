package com.minerva.minervaapi.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(

        @NotBlank(message = "Informe seu nome")
        @Size(max = 100, message = "O nome deve ter menos de 100 caracteres")
        @Size(min = 3, message = "O nome deve ter mais de 3 caracteres")
        String name
) { }
