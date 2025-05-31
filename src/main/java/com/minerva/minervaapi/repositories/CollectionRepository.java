package com.minerva.minervaapi.repositories;

import com.minerva.minervaapi.models.Collection;
import com.minerva.minervaapi.models.CollectionPk;
import com.minerva.minervaapi.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, CollectionPk> {
    Page<Collection> findAllByUserAndDeck_TitleContainingIgnoreCase(User user, String search, Pageable pageable);
}