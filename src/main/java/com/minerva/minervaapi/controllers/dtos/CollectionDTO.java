package com.minerva.minervaapi.controllers.dtos;

import jakarta.validation.constraints.NotBlank;

public record CollectionDTO(
        @NotBlank(message = "Informe o ID do deck")
        String deckId
) { }
