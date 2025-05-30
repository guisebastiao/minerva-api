package com.minerva.minervaapi.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DeckDTO(

        @NotBlank(message = "Informe o título da coleção de flashcards")
        @Size(max = 50, message = "O título deve ter menos de 50 caracteres")
        String title,

        @NotBlank(message = "Informe a descrição da coleção de flashcards")
        @Size(max = 200, message = "A descrição deve ter menos de 200 caracteres")
        String description,

        @NotNull(message = "Informe se sua coleção de flashcards é publica")
        Boolean isPublic
){ }
