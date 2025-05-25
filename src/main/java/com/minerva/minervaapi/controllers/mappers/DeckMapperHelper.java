package com.minerva.minervaapi.controllers.mappers;

import com.minerva.minervaapi.controllers.dtos.ReviewResponseDTO;
import com.minerva.minervaapi.models.*;
import com.minerva.minervaapi.repositories.AssessmentRepository;
import com.minerva.minervaapi.repositories.CollectionRepository;
import com.minerva.minervaapi.repositories.ReviewRepository;
import com.minerva.minervaapi.security.AuthProvider;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class DeckMapperHelper {

    @Autowired
    private AuthProvider authProvider;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FlashcardMapper flashcardMapper;

    @Named("isBelongsToAuthUser")
    public Boolean isBelongsToAuthUser(Deck deck) {
        return authProvider.getAuthenticatedUser().getId().equals(deck.getUser().getId());
    }

    @Named("findAssessment")
    public Double findAssessment(Deck deck) {
        List<Assessment> assessment = this.assessmentRepository.findAllByDeck(deck);

        return assessment.stream()
                .mapToInt(Assessment::getAssessmentValue)
                .average()
                .orElse(0.0);
    }

    @Named("isBelongsToCollectionUser")
    public Boolean isBelongsToCollectionUser(Deck deck) {
        User user = authProvider.getAuthenticatedUser();
        CollectionPk pk = this.generateCollectionPk(user, deck);
        Optional<Collection> collection = this.collectionRepository.findById(pk);
        return collection.isPresent();
    }

    @Named("findStudyCollection")
    public ReviewResponseDTO findStudyCollection(Deck deck) {
        LocalDate currentDate = LocalDate.now();
        User user = authProvider.getAuthenticatedUser();

        List<Review> reviewsFound = reviewRepository.findAllByDeckAndUser(deck, user);

        List<Flashcard> flashcardsToStudy = reviewsFound.stream()
                .filter(review -> review.getNextReviewDate() == null || !currentDate.isBefore(review.getNextReviewDate()))
                .map(Review::getFlashcard)
                .distinct()
                .toList();

        return new ReviewResponseDTO(flashcardsToStudy.size(), deck.getFlashcards().size(), flashcardsToStudy.isEmpty());
    }

    private CollectionPk generateCollectionPk(User user, Deck deck) {
        CollectionPk collectionPk = new CollectionPk();
        collectionPk.setUserId(user.getId());
        collectionPk.setDeckId(deck.getId());
        return collectionPk;
    }
}