package com.minerva.minervaapi.controllers.dtos;

public record CollectionResponseDTO(
    Boolean favorite,
    DeckResponseDTO deck
) { }
