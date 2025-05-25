package com.minerva.minervaapi.controllers.dtos;

public record ReviewResponseDTO(
        Integer toStudy,
        Integer totalFlashcards,
        Boolean isUpToDate
){ }
