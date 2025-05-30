package com.minerva.minervaapi.controllers;

import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.services.FlashcardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flashcards")
public class FlashcardController {

    @Autowired
    private FlashcardService flashcardService;

    @DeleteMapping("/{flashcardId}")
    public ResponseEntity<DefaultDTO> deleteFlashcard(@PathVariable String flashcardId) {
        DefaultDTO response = this.flashcardService.deleteFlashcard(flashcardId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
