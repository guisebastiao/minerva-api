package com.minerva.minervaapi.controllers.mappers;

import com.minerva.minervaapi.controllers.dtos.CollectionResponseDTO;
import com.minerva.minervaapi.models.Collection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DeckMapper.class})
public interface CollectionMapper {
    CollectionResponseDTO toDTO(Collection collection);
}
