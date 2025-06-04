package com.minerva.minervaapi.services.impl;

import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.controllers.dtos.ReviewDTO;
import com.minerva.minervaapi.exceptions.BadRequestException;
import com.minerva.minervaapi.exceptions.EntityNotFoundException;
import com.minerva.minervaapi.models.Flashcard;
import com.minerva.minervaapi.models.Review;
import com.minerva.minervaapi.models.User;
import com.minerva.minervaapi.repositories.FlashcardRepository;
import com.minerva.minervaapi.repositories.ReviewRepository;
import com.minerva.minervaapi.security.AuthProvider;
import com.minerva.minervaapi.services.ReviewService;
import com.minerva.minervaapi.utils.UUIDConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AuthProvider authProvider;

    @Override
    public DefaultDTO reviewFlashcard(String flashcardId, ReviewDTO reviewDTO) {
        Flashcard flashcard = this.findFlashcardById(flashcardId);
        User user = this.getAuthenticatedUser();
        Review review = this.findReviewByUserAndFlashcard(user, flashcard);

        int quality = reviewDTO.rating();

        Review appliedAlgorithmInReview = this.applySM2Algorithm(review, quality);

        reviewRepository.save(appliedAlgorithmInReview);

        return new DefaultDTO("Flashcard revisado com sucesso", Boolean.TRUE, null, null, null);
    }

    private Flashcard findFlashcardById(String flashcardId) {
        return this.flashcardRepository.findById(UUIDConverter.toUUID(flashcardId))
                .orElseThrow(() -> new EntityNotFoundException("O flashcard não foi encontrado"));
    }

    private Review findReviewByUserAndFlashcard(User user, Flashcard flashcard) {
        return this.reviewRepository.findByUserAndFlashcard(user, flashcard)
                .orElseThrow(() -> new BadRequestException("Você não possuí esse flashcard como estudo pendente"));
    }

    private User getAuthenticatedUser() {
        return this.authProvider.getAuthenticatedUser();
    }

    private Review applySM2Algorithm(Review review, Integer quality) {
        int sm2Quality = this.mapToSm2Quality(quality);

        if (sm2Quality < 3) {
            review.setRepetition(0);
            review.setDaysInterval(1);
        } else {
            int newInterval;
            if (review.getRepetition() == 0) {
                newInterval = 1;
            } else if (review.getRepetition() == 1) {
                newInterval = 6;
            } else {
                newInterval = (int) Math.round(review.getDaysInterval() * review.getEasinessFactor());
            }
            review.setDaysInterval(newInterval);
            review.setRepetition(review.getRepetition() + 1);
        }

        double delta = (0.1 - (5 - sm2Quality) * (0.08 + (5 - sm2Quality) * 0.02));
        double newEf = review.getEasinessFactor() + delta;
        if (newEf < 1.3) {
            newEf = 1.3;
        }
        review.setEasinessFactor(newEf);

        review.setReviewedAt(LocalDateTime.now());
        review.setNextReviewDate(LocalDate.now().plusDays(review.getDaysInterval()));

        return review;
    }

    private int mapToSm2Quality(int userQuality) {
        switch (userQuality) {
            case 0: return 0;
            case 1: return 3;
            case 2: return 4;
            case 3: return 5;
            default: return 0;
        }
    }
}
