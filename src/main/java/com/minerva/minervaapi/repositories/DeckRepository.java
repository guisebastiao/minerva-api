package com.minerva.minervaapi.repositories;

import com.minerva.minervaapi.models.Deck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeckRepository extends JpaRepository<Deck, UUID> {
    Page<Deck> findAllByIsPublicAndTitleContainingIgnoreCase(boolean isPublic, String title, Pageable pageable);
}
