package com.minerva.minervaapi.repositories;

import com.minerva.minervaapi.models.Deck;
import com.minerva.minervaapi.models.Flashcard;
import com.minerva.minervaapi.models.Review;
import com.minerva.minervaapi.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findAllByDeckAndUser(Deck deck, User user);
    Optional<Review> findByUserAndFlashcard(User user, Flashcard flashcard);
    boolean existsByDeckAndUserAndFlashcard(Deck deck, User user, Flashcard flashcard);
    Page<Review> findByDeckAndUserAndNextReviewDateLessThanEqualOrNextReviewDateIsNull(Deck deck, User user, LocalDate currentDate, Pageable pageable);
}
