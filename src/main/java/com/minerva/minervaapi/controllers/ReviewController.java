package com.minerva.minervaapi.controllers;

import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.controllers.dtos.ReviewDTO;
import com.minerva.minervaapi.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/{flashcardId}")
    public ResponseEntity<DefaultDTO> reviewFlashcard(@PathVariable String flashcardId, @RequestBody @Valid ReviewDTO reviewDTO) {
        DefaultDTO response = this.reviewService.reviewFlashcard(flashcardId, reviewDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
