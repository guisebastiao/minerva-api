package com.minerva.minervaapi.controllers.mappers;

import com.minerva.minervaapi.controllers.dtos.DeckDTO;
import com.minerva.minervaapi.controllers.dtos.DeckResponseDTO;
import com.minerva.minervaapi.models.Deck;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeckMapper {

    DeckDTO toDTO(Deck deck);
    DeckResponseDTO toResponseDTO(Deck deck);
    Deck toEntity(DeckDTO deckDTO);
    Deck toEntity(DeckResponseDTO deckDTO);
}
