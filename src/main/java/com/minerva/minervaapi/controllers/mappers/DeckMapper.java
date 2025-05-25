package com.minerva.minervaapi.controllers.mappers;

import com.minerva.minervaapi.controllers.dtos.DeckDTO;
import com.minerva.minervaapi.controllers.dtos.DeckResponseDTO;
import com.minerva.minervaapi.models.Deck;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DeckMapperHelper.class, UserMapper.class})
public interface DeckMapper {

    @Mapping(target = "belongsToAuthUser", source = ".", qualifiedByName = "isBelongsToAuthUser")
    @Mapping(target = "assessment", source = ".", qualifiedByName = "findAssessment")
    @Mapping(target = "belongsToCollectionUser", source = ".", qualifiedByName = "isBelongsToCollectionUser")
    @Mapping(target = "review", source = ".", qualifiedByName = "findStudyCollection")
    DeckResponseDTO toResponseDTO(Deck deck);

    Deck toEntity(DeckDTO deckDTO);
}
