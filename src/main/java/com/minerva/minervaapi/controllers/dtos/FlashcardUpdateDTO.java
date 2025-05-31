package com.minerva.minervaapi.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

public record FlashcardUpdateDTO(

        @UUID(message = "Formato inválido")
        String id,

        @NotBlank(message = "Informe a pergunta")
        @Size(max = 300, message = "A pergunta deve ter menos de 300 caracteres")
        String question,

        @NotBlank(message = "Informe a resposta")
        @Size(max = 300, message = "A resposta deve ter menos de 300 caracteres")
        String answer
) {
}
