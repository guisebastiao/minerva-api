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

@Repository
public interface CollectionRepository extends JpaRepository<Collection, CollectionPk> {
    @Query("SELECT c FROM Collection c WHERE c.user = :user AND (:search IS NULL OR :search = '' OR LOWER(TRIM(c.deck.title)) LIKE LOWER(CONCAT('%', TRIM(:search), '%'))) ORDER BY c.favorite DESC, LOWER(c.deck.title) ASC")
    Page<Collection> findByUserAndDeckTitle(@Param("user") User user, @Param("search") String search, Pageable pageable);
}