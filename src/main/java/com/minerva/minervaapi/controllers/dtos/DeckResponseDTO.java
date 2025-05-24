package com.minerva.minervaapi.controllers.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeckResponseDTO(

      UUID id,
      String title,
      String description,
      Boolean isPublic,
      UUID publicId,
      LocalDateTime createdAt
){ }
