package com.minerva.minervaapi.repositories;

import com.minerva.minervaapi.models.Deck;
import com.minerva.minervaapi.models.Flashcard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FlashcardRepository extends JpaRepository<Flashcard, UUID> {
    List<Flashcard> findAllByIdIn(List<UUID> ids);
    Page<Flashcard> findAllByDeck(Deck deck, Pageable pageable);
}
