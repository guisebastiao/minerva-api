package com.minerva.minervaapi.controllers;

import com.minerva.minervaapi.controllers.dtos.DeckDTO;
import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.services.DeckService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/decks")
public class DeckController {

    @Autowired
    private DeckService deckService;

    @PostMapping
    public ResponseEntity<DefaultDTO> createDeck(@RequestBody @Valid DeckDTO deckDTO) {
        DefaultDTO response = this.deckService.createDeck(deckDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{deckId}")
    public ResponseEntity<DefaultDTO> findDeckById(@PathVariable String deckId) {
        DefaultDTO response = this.deckService.findDeckById(deckId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{deckId}")
    public ResponseEntity<DefaultDTO> updateDeck(@RequestBody @Valid DeckDTO deckDTO, @PathVariable String deckId) {
        DefaultDTO response = this.deckService.updateDeck(deckDTO, deckId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{deckId}")
    public ResponseEntity<DefaultDTO> deleteDeck(@PathVariable String deckId) {
        DefaultDTO response = this.deckService.deleteDeck(deckId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
