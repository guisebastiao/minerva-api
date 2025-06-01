package com.minerva.minervaapi.controllers.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DeckResponseDTO(
      UUID id,
      String title,
      String description,
      Boolean isPublic,
      LocalDateTime createdAt,
      UserResponseDTO user,
      Boolean belongsToAuthUser,
      Boolean belongsToCollectionUser,
      Boolean authUserAssessmentDeck,
      Double assessment,
      ReviewResponseDTO review,
      List<FlashcardResponseDTO> flashcards
){ }
