package com.minerva.minervaapi.services;

import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.controllers.dtos.ReviewDTO;

public interface ReviewService {
    DefaultDTO reviewFlashcard(String flashcardId, ReviewDTO reviewDTO);
}
