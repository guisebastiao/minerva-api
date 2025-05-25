package com.minerva.minervaapi.repositories;

import com.minerva.minervaapi.models.Assessment;
import com.minerva.minervaapi.models.AssessmentPk;
import com.minerva.minervaapi.models.Deck;
import com.minerva.minervaapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssessmentRepository extends JpaRepository<Assessment, AssessmentPk> {
    List<Assessment> findAllByDeck(Deck deck);
    Optional<Assessment> findByDeckAndUser(Deck deck, User user);
}
