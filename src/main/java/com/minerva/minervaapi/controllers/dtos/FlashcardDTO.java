package com.minerva.minervaapi.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FlashcardDTO(
        @NotBlank(message = "Informe a pergunta")
        @Size(max = 300, message = "A pergunta deve ter menos de 300 caracteres")
        String question,

        @NotBlank(message = "Informe a resposta")
        @Size(max = 300, message = "A resposta deve ter menos de 300 caracteres")
        String answer
) {
}
