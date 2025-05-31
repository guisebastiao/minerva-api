package com.minerva.minervaapi.repositories;

import com.minerva.minervaapi.models.Collection;
import com.minerva.minervaapi.models.CollectionPk;
import com.minerva.minervaapi.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, CollectionPk> {
    @Query(value = """
        SELECT c.*
        FROM collections c
        JOIN decks d ON c.deck_id = d.id
        WHERE c.user_id = :userId
        AND lower(unaccent(d.title)) LIKE lower(unaccent(concat('%', :search, '%')))
        ORDER BY c.favorite DESC, lower(d.title) ASC
    """, nativeQuery = true)
    Page<Collection> findByUserAndDeckTitle(@Param("userId") UUID userId, @Param("search") String search, Pageable pageable);
}