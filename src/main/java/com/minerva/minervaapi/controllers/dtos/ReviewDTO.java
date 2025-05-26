package com.minerva.minervaapi.controllers.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReviewDTO(
        @NotNull(message = "Informe a pontuação")
        @Min(value = 0, message = "O valor minimo da pontuação é 0")
        @Max(value = 3, message = "O valor máximo da pontuação é 3")
        Integer rating
) {
}
