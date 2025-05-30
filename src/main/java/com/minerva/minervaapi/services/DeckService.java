package com.minerva.minervaapi.services;

import com.minerva.minervaapi.controllers.dtos.DeckDTO;
import com.minerva.minervaapi.controllers.dtos.DeckUpdateDTO;
import com.minerva.minervaapi.controllers.dtos.DefaultDTO;

public interface DeckService {
    DefaultDTO createDeck(DeckDTO deckDTO);
    DefaultDTO findDeckById(String deckId);
    DefaultDTO updateDeck(DeckUpdateDTO deckDTO, String deckId);
    DefaultDTO deleteDeck(String deckId);
}
