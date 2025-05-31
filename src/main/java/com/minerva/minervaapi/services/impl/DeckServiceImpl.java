package com.minerva.minervaapi.services.impl;

import com.minerva.minervaapi.controllers.dtos.*;
import com.minerva.minervaapi.controllers.mappers.DeckMapper;
import com.minerva.minervaapi.controllers.mappers.FlashcardMapper;
import com.minerva.minervaapi.exceptions.BadRequestException;
import com.minerva.minervaapi.exceptions.EntityNotFoundException;
import com.minerva.minervaapi.exceptions.UnauthorizedException;
import com.minerva.minervaapi.models.*;
import com.minerva.minervaapi.models.Collection;
import com.minerva.minervaapi.repositories.CollectionRepository;
import com.minerva.minervaapi.repositories.DeckRepository;
import com.minerva.minervaapi.repositories.FlashcardRepository;
import com.minerva.minervaapi.repositories.ReviewRepository;
import com.minerva.minervaapi.security.AuthProvider;
import com.minerva.minervaapi.services.DeckService;
import com.minerva.minervaapi.utils.UUIDConverter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class DeckServiceImpl implements DeckService {

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AuthProvider authProvider;

    @Autowired
    private DeckMapper deckMapper;

    @Autowired
    private FlashcardMapper flashcardMapper;

    @Override
    @Transactional
    public DefaultDTO createDeck(DeckDTO deckDTO) {
        User user = this.getAuthenticatedUser();

        Deck deck = this.deckMapper.toEntity(deckDTO);
        deck.setUser(user);
        deck.setPublicId(UUID.randomUUID());
        deck.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));

        List<Flashcard> flashcardList = flashcardMapper.toEntities(deckDTO.flashcards());
        flashcardList.forEach(flashcard -> flashcard.setDeck(deck));
        deck.setFlashcards(flashcardList);

        Deck savedDeck = this.deckRepository.save(deck);

        Collection collection = new Collection();
        collection.setUser(user);
        collection.setDeck(savedDeck);
        this.collectionRepository.save(collection);

        this.flashcardRepository.saveAll(flashcardList);
        
        List<Review> reviews = flashcardList.stream().map(flashcard -> {
            Review review = new Review();
            review.setDeck(savedDeck);
            review.setUser(user);
            review.setFlashcard(flashcard);
            return review;
        }).toList();

        this.reviewRepository.saveAll(reviews);

        return new DefaultDTO("Coleção criada com sucesso", Boolean.TRUE, null, null, null);
    }

    @Override
    public DefaultDTO findDeckById(String deckId) {
        Deck deck = this.findCollectionById(deckId);

        if(!deck.getIsPublic() && !deck.getUser().getId().equals(this.getAuthenticatedUser().getId())) {
            throw new UnauthorizedException("Você não tem permissão para visualizar essa coleção");
        }

        DeckResponseDTO deckResponseDTO = this.deckMapper.toResponseDTO(deck);
        return new DefaultDTO("Coleção encontrada com sucesso", Boolean.TRUE, deckResponseDTO, null, null);
    }

    @Override
    @Transactional
    public DefaultDTO updateDeck(DeckUpdateDTO deckUpdateDTO, String deckId) {
        Deck deck = this.findCollectionById(deckId);
        this.checkDeckCreator(deck.getUser());

        deck.setTitle(deckUpdateDTO.title());
        deck.setDescription(deckUpdateDTO.description());
        deck.setIsPublic(deckUpdateDTO.isPublic());

        this.deckRepository.save(deck);

        List<Flashcard> flashcardsToSave = new ArrayList<>();

        for (FlashcardUpdateDTO dto : deckUpdateDTO.flashcards()) {
            Flashcard flashcard;

            if (dto.id() != null && !dto.id().isBlank()) {
                UUID flashcardUUID = UUIDConverter.toUUID(dto.id());

                Optional<Flashcard> optionalFlashcard = flashcardRepository.findById(flashcardUUID);

                if (optionalFlashcard.isPresent()) {
                    flashcard = optionalFlashcard.get();

                    if (!flashcard.getDeck().getUser().equals(deck.getUser())) {
                        throw new UnauthorizedException("Você não tem permissão para alterar esse flashcard");
                    }

                } else {
                    flashcard = new Flashcard();
                    flashcard.setId(flashcardUUID);
                    flashcard.setDeck(deck);
                }
            } else {
                flashcard = new Flashcard();
                flashcard.setDeck(deck);
            }

            flashcard.setQuestion(dto.question());
            flashcard.setAnswer(dto.answer());

            flashcardsToSave.add(flashcard);
        }

        List<Flashcard> savedFlashcards = this.flashcardRepository.saveAll(flashcardsToSave);
        User user = this.getAuthenticatedUser();

        List<Flashcard> flashcardsWithoutReview = savedFlashcards.stream()
                .filter(flashcard -> !reviewRepository.existsByDeckAndUserAndFlashcard(deck, user, flashcard))
                .toList();

        List<Review> reviews = flashcardsWithoutReview.stream()
                .map(flashcard -> {
                    Review review = new Review();
                    review.setDeck(deck);
                    review.setUser(user);
                    review.setFlashcard(flashcard);
                    return review;
                }).toList();

        reviewRepository.saveAll(reviews);

        return new DefaultDTO("Coleção atualizada com sucesso", Boolean.TRUE, null, null, null);
    }

    @Override
    @Transactional
    public DefaultDTO deleteDeck(String deckId) {
        Deck deck = this.findCollectionById(deckId);
        this.checkDeckCreator(deck.getUser());
        this.deckRepository.delete(deck);
        return new DefaultDTO("Coleção excluida com sucesso", Boolean.TRUE, null, null, null);
    }

    private User getAuthenticatedUser() {
        return authProvider.getAuthenticatedUser();
    }

    private Deck findCollectionById(String deckId) {
        return this.deckRepository.findById(UUIDConverter.toUUID(deckId))
                .orElseThrow(() -> new EntityNotFoundException("Coleção não foi encontrada"));
    }

    private void checkDeckCreator(User creator) {
        if(!creator.getId().equals(this.getAuthenticatedUser().getId())) {
            throw new UnauthorizedException("Você não tem permissão para acessar esta coleção");
        }
    }

    private List<Flashcard> findAllFlashcardByIds(List<UUID> flashcardIds) {
        List<Flashcard> flashcards = flashcardRepository.findAllByIdIn(flashcardIds);

        if(flashcards.size() != flashcardIds.size()) {
            throw new BadRequestException("Alguns IDs dos flashcards não foram encontrados");
        }

        return flashcards;
    }

    private void checkFlashcardBelongToUser(List<User> users) {
        User authenticatedUser = this.getAuthenticatedUser();

        users.forEach(user -> {
            if(!user.getId().equals(authenticatedUser.getId())) {
                throw new UnauthorizedException("Você não tem permissão para acessar este flashcard");
            }
        });
    }
}
