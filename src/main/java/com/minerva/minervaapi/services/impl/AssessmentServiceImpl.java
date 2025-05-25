package com.minerva.minervaapi.services.impl;

import com.minerva.minervaapi.controllers.dtos.AssessmentDTO;
import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.exceptions.DuplicateEntityException;
import com.minerva.minervaapi.exceptions.EntityNotFoundException;
import com.minerva.minervaapi.models.*;
import com.minerva.minervaapi.repositories.AssessmentRepository;
import com.minerva.minervaapi.repositories.DeckRepository;
import com.minerva.minervaapi.security.AuthProvider;
import com.minerva.minervaapi.services.AssessmentService;
import com.minerva.minervaapi.utils.UUIDConverter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssessmentServiceImpl implements AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private AuthProvider authProvider;

    @Override
    @Transactional
    public DefaultDTO createAssessment(String deckId, AssessmentDTO assessmentDTO) {
        Deck deck = this.findDeckById(deckId);
        User user = this.getAuthenticatedUser();

        this.userHasAlreadyRated(user, deck);

        Assessment assessment = new Assessment();
        assessment.setDeck(deck);
        assessment.setUser(user);
        assessment.setAssessmentValue(assessmentDTO.assessmentValue());

        this.assessmentRepository.save(assessment);

        return new DefaultDTO("Avaliação criada com sucesso", Boolean.TRUE, null, null, null);
    }

    @Override
    @Transactional
    public DefaultDTO deleteAssessment(String deckId) {
        Deck deck = this.findDeckById(deckId);
        User user = this.getAuthenticatedUser();

        AssessmentPk pk = this.generateAssessmentPk(user, deck);

        Assessment assessment = this.assessmentRepository.findById(pk)
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não foi encontrada"));

        this.assessmentRepository.delete(assessment);

        return new DefaultDTO("Avaliação deletada com sucesso", Boolean.TRUE, null, null, null);
    }

    private Deck findDeckById(String deckId) {
        return this.deckRepository.findById(UUIDConverter.toUUID(deckId))
                .orElseThrow(() -> new EntityNotFoundException("Coleção não foi encontrada"));
    }

    private User getAuthenticatedUser() {
        return authProvider.getAuthenticatedUser();
    }

    private AssessmentPk generateAssessmentPk(User user, Deck deck) {
        AssessmentPk assessmentPk = new AssessmentPk();
        assessmentPk.setUserId(user.getId());
        assessmentPk.setDeckId(deck.getId());
        return assessmentPk;
    }

    private void userHasAlreadyRated(User user, Deck deck) {
        AssessmentPk pk = this.generateAssessmentPk(user, deck);
        Optional<Assessment> assessment = this.assessmentRepository.findById(pk);

        if (assessment.isPresent()) {
            throw new DuplicateEntityException("Você já fez uma avaliação para essa coleção");
        }
    }
}
