package com.minerva.minervaapi.repositories;

import com.minerva.minervaapi.models.Collection;
import com.minerva.minervaapi.models.CollectionPk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, UUID> {
    Optional<Collection> findById(CollectionPk collectionPk);
    Page<Collection> findAllByUserId(UUID userId, Pageable pageable);
}
