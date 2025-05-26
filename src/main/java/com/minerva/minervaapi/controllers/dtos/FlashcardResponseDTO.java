package com.minerva.minervaapi.controllers.dtos;

import java.util.UUID;

public record FlashcardResponseDTO(
    UUID id,
    String question,
    String answer
) { }
