package com.minerva.minervaapi.services;

import com.minerva.minervaapi.controllers.dtos.DefaultDTO;

public interface FlashcardService {
    DefaultDTO findAllFlashcards(String deckId, int offset, int limit);
    DefaultDTO deleteFlashcard(String flashcardId);
}
