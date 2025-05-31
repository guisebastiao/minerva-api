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
    @Query(value = """
        SELECT * FROM decks d
        WHERE d.is_public = true
        AND lower(unaccent(d.title)) LIKE lower(unaccent(concat('%', :title, '%')))
    """, nativeQuery = true)
    Page<Deck> findByTitle(@Param("title") String title, Pageable pageable);
}
