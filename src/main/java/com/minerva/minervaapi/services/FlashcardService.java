package com.minerva.minervaapi.services;

import com.minerva.minervaapi.controllers.dtos.DefaultDTO;

public interface FlashcardService {
    DefaultDTO deleteFlashcard(String flashcardId);
}
