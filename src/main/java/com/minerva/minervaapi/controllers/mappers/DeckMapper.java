package com.minerva.minervaapi.controllers.mappers;

import com.minerva.minervaapi.controllers.dtos.DeckDTO;
import com.minerva.minervaapi.controllers.dtos.DeckResponseDTO;
import com.minerva.minervaapi.models.Deck;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DeckMapperHelper.class})
public interface DeckMapper {

    @Mapping(target = "belongsToAuthUser", source = ".", qualifiedByName = "isAuthUser")
    DeckResponseDTO toResponseDTO(Deck deck);

    DeckDTO toDTO(Deck deck);
    Deck toEntity(DeckDTO deckDTO);
    Deck toEntity(DeckResponseDTO deckDTO);
}
