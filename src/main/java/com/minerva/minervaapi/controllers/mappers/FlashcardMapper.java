package com.minerva.minervaapi.controllers.mappers;

import com.minerva.minervaapi.controllers.dtos.FlashcardDTO;
import com.minerva.minervaapi.controllers.dtos.FlashcardUpdateDTO;
import com.minerva.minervaapi.models.Flashcard;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FlashcardMapper {

    FlashcardDTO toDTO(Flashcard flashcard);
    FlashcardUpdateDTO toUpdateDTO(Flashcard flashcard);
    Flashcard toEntity(FlashcardDTO flashcardDTO);
    List<Flashcard> toEntities(List<FlashcardDTO> flashcards);

}
