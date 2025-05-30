package com.minerva.minervaapi.services;

import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.controllers.dtos.FlashcardDTO;
import com.minerva.minervaapi.controllers.dtos.FlashcardUpdateDTO;

import java.util.List;

public interface FlashcardService {

    DefaultDTO createFlashcards(String deckId, List<FlashcardDTO> flashcards);
    DefaultDTO updateFlashcards(List<FlashcardUpdateDTO> flashcards);
    DefaultDTO deleteFlashcard(String flashcardId);
}
