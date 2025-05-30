package com.minerva.minervaapi.services.impl;

import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.controllers.dtos.FlashcardDTO;
import com.minerva.minervaapi.controllers.dtos.FlashcardUpdateDTO;
import com.minerva.minervaapi.controllers.mappers.FlashcardMapper;
import com.minerva.minervaapi.exceptions.BadRequestException;
import com.minerva.minervaapi.exceptions.EntityNotFoundException;
import com.minerva.minervaapi.exceptions.UnauthorizedException;
import com.minerva.minervaapi.models.Deck;
import com.minerva.minervaapi.models.Flashcard;
import com.minerva.minervaapi.models.Review;
import com.minerva.minervaapi.models.User;
import com.minerva.minervaapi.repositories.DeckRepository;
import com.minerva.minervaapi.repositories.FlashcardRepository;
import com.minerva.minervaapi.repositories.ReviewRepository;
import com.minerva.minervaapi.security.AuthProvider;
import com.minerva.minervaapi.services.FlashcardService;
import com.minerva.minervaapi.utils.UUIDConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FlashcardServiceImpl implements FlashcardService {

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FlashcardMapper flashcardMapper;

    @Autowired
    private AuthProvider authProvider;

    @Override
    public DefaultDTO updateFlashcards(List<FlashcardUpdateDTO> flashcards) {
        List<UUID> ids = flashcards.stream()
                .map(dto -> UUIDConverter.toUUID(dto.flashcardId()))
                .toList();

        List<Flashcard> flashcardEntity = this.findAllFlashcardByIds(ids);

        List<User> users = flashcardEntity.stream()
                .map(dto -> dto.getDeck().getUser())
                .toList();

        this.checkFlashcardBelongToUser(users);

        Map<UUID, FlashcardUpdateDTO> dtoMap = flashcards.stream()
                .collect(Collectors.toMap(dto -> UUIDConverter.toUUID(dto.flashcardId()), Function.identity()));

        for(Flashcard flashcard : flashcardEntity) {
            FlashcardUpdateDTO dto = dtoMap.get(flashcard.getId());
            flashcard.setQuestion(dto.question());
            flashcard.setAnswer(dto.answer());
        }

        this.flashcardRepository.saveAll(flashcardEntity);

        return new DefaultDTO("Flashcards atualizados com sucesso", true, null, null, null);
    }

    @Override
    public DefaultDTO deleteFlashcard(String flashcardId) {
        Flashcard flashcard = this.findFlashcardById(flashcardId);

        this.checkFlashcardBelongToUser(flashcard.getDeck().getUser());

        this.flashcardRepository.delete(flashcard);

        return new DefaultDTO("Flashcard deletado com sucesso", true, null, null, null);
    }

    private Deck findDeckById(String deckId) {
        return this.deckRepository.findById(UUIDConverter.toUUID(deckId))
                .orElseThrow(() -> new EntityNotFoundException("Coleção não foi encontrada"));
    }

    private List<Flashcard> findAllFlashcardByIds(List<UUID> flashcardIds) {
        List<Flashcard> flashcards = flashcardRepository.findAllByIdIn(flashcardIds);

        if(flashcards.size() != flashcardIds.size()) {
            throw new BadRequestException("Alguns IDs dos flashcards não foram encontrados");
        }

        return flashcards;
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

    private void checkFlashcardBelongToUser(List<User> users) {
        User authenticatedUser = this.getAuthenticatedUser();

        users.forEach(user -> {
            if(!user.getId().equals(authenticatedUser.getId())) {
                throw new UnauthorizedException("Você não tem permissão para acessar este flashcard");
            }
        });
    }

    private User getAuthenticatedUser() {
        return this.authProvider.getAuthenticatedUser();
    }
}
