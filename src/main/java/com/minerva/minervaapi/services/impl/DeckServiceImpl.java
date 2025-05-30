package com.minerva.minervaapi.services.impl;

import com.minerva.minervaapi.controllers.dtos.DeckDTO;
import com.minerva.minervaapi.controllers.dtos.DeckResponseDTO;
import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.controllers.mappers.DeckMapper;
import com.minerva.minervaapi.controllers.mappers.FlashcardMapper;
import com.minerva.minervaapi.exceptions.EntityNotFoundException;
import com.minerva.minervaapi.exceptions.UnauthorizedException;
import com.minerva.minervaapi.models.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public DefaultDTO updateDeck(DeckDTO deckDTO, String deckId) {
        Deck deck = this.findCollectionById(deckId);

        this.checkDeckCreator(deck.getUser());

        deck.setTitle(deckDTO.title());
        deck.setDescription(deckDTO.description());
        deck.setIsPublic(deckDTO.isPublic());

        this.deckRepository.save(deck);
        return new DefaultDTO("Coleção atualizada com sucesso", Boolean.TRUE, null, null, null);
    }

    @Override
    @Transactional
    public DefaultDTO deleteDeck(String deckId) {
        Deck deck = this.findCollectionById(deckId);
        this.checkDeckCreator(deck.getUser());
        this.deckRepository.delete(deck);
        return new DefaultDTO("Coleção deletada com sucesso", Boolean.TRUE, null, null, null);
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
}
