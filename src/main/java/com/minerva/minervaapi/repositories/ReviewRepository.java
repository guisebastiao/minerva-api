package com.minerva.minervaapi.repositories;

import com.minerva.minervaapi.models.Deck;
import com.minerva.minervaapi.models.Flashcard;
import com.minerva.minervaapi.models.Review;
import com.minerva.minervaapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Optional<Review> findByFlashcardAndDeckAndUser(Flashcard flashcard, Deck deck, User user);
    List<Review> findByFlashcardAndDeck(Flashcard flashcard, Deck deck);
    List<Review> findAllByDeckAndUser(Deck deck, User user);
}
