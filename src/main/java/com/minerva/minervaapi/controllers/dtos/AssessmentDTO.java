package com.minerva.minervaapi.controllers.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AssessmentDTO(
        @NotNull(message = "Informe o valor da avaliação")
        @Max(value = 5, message = "O valor máximo da avalição é 5")
        @Min(value = 1, message = "O valor minimo da avalição é 1")
        Integer assessmentValue
) { }
