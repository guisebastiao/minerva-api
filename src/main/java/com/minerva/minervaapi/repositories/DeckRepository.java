package com.minerva.minervaapi.repositories;

import com.minerva.minervaapi.models.Deck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeckRepository extends JpaRepository<Deck, UUID> {
    @Query("SELECT d FROM Deck d WHERE d.isPublic = true AND (:search IS NULL OR :search = '' OR LOWER(TRIM(d.title)) LIKE LOWER(CONCAT('%', TRIM(:search), '%')))")
    Page<Deck> findByTitle(@Param("search") String search, Pageable pageable);
}
