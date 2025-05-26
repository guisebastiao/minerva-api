package com.minerva.minervaapi.controllers;

import com.minerva.minervaapi.controllers.dtos.CollectionDTO;
import com.minerva.minervaapi.controllers.dtos.CollectionFavoriteDTO;
import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.services.CollectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collections")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @PostMapping
    public ResponseEntity<DefaultDTO> addNewCollection(@RequestBody @Valid CollectionDTO collectionDTO) {
        DefaultDTO response = this.collectionService.addNewCollection(collectionDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/favorite/{deckId}")
    public ResponseEntity<DefaultDTO> addFavorite(@PathVariable String deckId, @RequestBody @Valid CollectionFavoriteDTO collectionFavoriteDTO) {
        DefaultDTO response = this.collectionService.addFavorite(deckId, collectionFavoriteDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<DefaultDTO> findAllCollections(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "20") int limit) {
        DefaultDTO response = this.collectionService.findAllCollections(offset, limit);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/to-study/{deckId}")
    public ResponseEntity<DefaultDTO> findAllCollectionsToStudy(@PathVariable String deckId, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "20") int limit) {
        DefaultDTO response = this.collectionService.findAllCollectionsToStudy(deckId, offset, limit);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{deckId}")
    public ResponseEntity<DefaultDTO> deleteCollection(@PathVariable String deckId) {
        DefaultDTO response = this.collectionService.deleteCollection(deckId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
