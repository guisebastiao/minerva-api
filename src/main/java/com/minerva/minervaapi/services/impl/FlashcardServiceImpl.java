package com.minerva.minervaapi.services.impl;

import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.controllers.dtos.FlashcardResponseDTO;
import com.minerva.minervaapi.controllers.dtos.PagingDTO;
import com.minerva.minervaapi.controllers.mappers.FlashcardMapper;
import com.minerva.minervaapi.exceptions.EntityNotFoundException;
import com.minerva.minervaapi.exceptions.UnauthorizedException;
import com.minerva.minervaapi.models.Deck;
import com.minerva.minervaapi.models.Flashcard;
import com.minerva.minervaapi.models.User;
import com.minerva.minervaapi.repositories.DeckRepository;
import com.minerva.minervaapi.repositories.FlashcardRepository;
import com.minerva.minervaapi.security.AuthProvider;
import com.minerva.minervaapi.services.FlashcardService;
import com.minerva.minervaapi.utils.UUIDConverter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FlashcardServiceImpl implements FlashcardService {

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private AuthProvider authProvider;

    @Autowired
    private FlashcardMapper flashcardMapper;

    @Override
    public DefaultDTO findAllFlashcards(String deckId, int offset, int limit) {
        Deck deck = this.findDeckById(deckId);

        Pageable pageable = PageRequest.of(offset, limit);

        Page<Flashcard> flashcardsPage  = this.flashcardRepository.findAllByDeck(deck, pageable);

        List<FlashcardResponseDTO> flashcards = flashcardsPage.getContent().stream()
                .map(flashcardMapper::toDTO)
                .toList();

        PagingDTO pagingDTO = new PagingDTO(flashcardsPage.getTotalElements(), flashcardsPage.getTotalPages(), offset, limit);

        return new DefaultDTO("Flashcards retornados", Boolean.TRUE, flashcards, pagingDTO, null);
    }

    @Override
    @Transactional
    public DefaultDTO deleteFlashcard(String flashcardId) {
        Flashcard flashcard = this.findFlashcardById(flashcardId);

        this.checkFlashcardBelongToUser(flashcard.getDeck().getUser());

        this.flashcardRepository.delete(flashcard);

        return new DefaultDTO("Flashcard excluido com sucesso", true, null, null, null);
    }

    private Deck findDeckById(String deckId) {
        return this.deckRepository.findById(UUIDConverter.toUUID(deckId))
                .orElseThrow(() -> new EntityNotFoundException("Coleção não foi encontrado"));
    }

    private Flashcard findFlashcardById(String flashcardId) {
        return this.flashcardRepository.findById(UUIDConverter.toUUID(flashcardId))
                .orElseThrow(() -> new EntityNotFoundException("Flashcard não foi encontrado"));
    }

    private void checkFlashcardBelongToUser(User user) {
        if(!user.getId().equals(this.getAuthenticatedUser().getId())) {
            throw new UnauthorizedException("Você não tem permissão para acessar este flashcard");
        }
    }

    private User getAuthenticatedUser() {
        return this.authProvider.getAuthenticatedUser();
    }
}
