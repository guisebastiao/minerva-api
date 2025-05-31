package com.minerva.minervaapi.services.impl;

import com.minerva.minervaapi.controllers.dtos.*;
import com.minerva.minervaapi.controllers.mappers.CollectionMapper;
import com.minerva.minervaapi.controllers.mappers.FlashcardMapper;
import com.minerva.minervaapi.exceptions.BadRequestException;
import com.minerva.minervaapi.exceptions.EntityNotFoundException;
import com.minerva.minervaapi.exceptions.UnauthorizedException;
import com.minerva.minervaapi.models.*;
import com.minerva.minervaapi.repositories.CollectionRepository;
import com.minerva.minervaapi.repositories.DeckRepository;
import com.minerva.minervaapi.repositories.ReviewRepository;
import com.minerva.minervaapi.security.AuthProvider;
import com.minerva.minervaapi.services.CollectionService;
import com.minerva.minervaapi.utils.UUIDConverter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private AuthProvider authProvider;

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private FlashcardMapper flashcardMapper;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    @Transactional
    public DefaultDTO addNewCollection(CollectionDTO collectionDTO) {
        Deck deck = this.findDeckById(UUIDConverter.toUUID(collectionDTO.deckId()));
        User user = this.getAuthenticatedUser();

        this.checkIsPublicAndCollectionBelongsUser(deck);

        CollectionPk pk = new CollectionPk();
        pk.setDeckId(deck.getId());
        pk.setUserId(user.getId());

        if (collectionRepository.findById(pk).isPresent()) {
            throw new BadRequestException("Essa Coleção já existe para esse usuario");
        }

        Collection collection = new Collection();
        collection.setDeck(deck);
        collection.setUser(user);

        this.collectionRepository.save(collection);

        List<Review> reviews = new ArrayList<>();

        deck.getFlashcards().forEach(flashcard -> {
            Review review = new Review();
            review.setDeck(deck);
            review.setUser(user);
            review.setFlashcard(flashcard);
            reviews.add(review);
        });

        this.reviewRepository.saveAll(reviews);

        return new DefaultDTO("Nova coleção adicionada com sucesso", Boolean.TRUE, null, null, null);
    }

    @Override
    @Transactional
    public DefaultDTO addFavorite(String deckId, CollectionFavoriteDTO collectionFavoriteDTO) {
        User user = this.getAuthenticatedUser();
        Deck deck = this.findDeckById(UUIDConverter.toUUID(deckId));

        this.checkIsPublicAndCollectionBelongsUser(deck);

        CollectionPk pk = this.generateCollectionPk(user, deck);

        Collection collection = this.findByCollectionPk(pk);

        collection.setId(pk);
        collection.setFavorite(collectionFavoriteDTO.favorite());

        this.collectionRepository.save(collection);

        String message = String.format(collectionFavoriteDTO.favorite() ? "Coleção adicionada como favorita" : "Coleção removida como favorita");

        return new DefaultDTO(message, Boolean.TRUE, null, null, null);
    }

    @Override
    public DefaultDTO findAllCollectionsToStudy(String deckId, int offset, int limit) {
        User user = this.getAuthenticatedUser();
        Deck deck = this.findDeckById(UUIDConverter.toUUID(deckId));

        LocalDate currentDate = LocalDate.now();
        Pageable pageable = PageRequest.of(offset, limit);

        Page<Review> resultPage = reviewRepository.findByDeckAndUserAndNextReviewDateLessThanEqualOrNextReviewDateIsNull(deck, user, currentDate, pageable);

        PagingDTO pagingDTO = new PagingDTO(resultPage.getTotalElements(), resultPage.getTotalPages(), offset, limit);

        List<FlashcardResponseDTO> data = resultPage.getContent().stream()
                .map(review -> {
                    Flashcard flashcard = review.getFlashcard();
                    return this.flashcardMapper.toDTO(flashcard);
                })
                .toList();


        return new DefaultDTO("Flashcards retornados com sucesso", Boolean.TRUE, data, pagingDTO, null);
    }

    @Override
    public DefaultDTO findAllCollections(String search, int offset, int limit) {
        User user = getAuthenticatedUser();

        Pageable pageable = PageRequest.of(offset, limit);

        Page<Collection> resultPage = collectionRepository.findAllByUserAndDeck_TitleContainingIgnoreCase(user, this.searchNormalize(search), pageable);

        PagingDTO pagingDTO = new PagingDTO(resultPage.getTotalElements(), resultPage.getTotalPages(), offset, limit);

        List<CollectionResponseDTO> data = resultPage.getContent().stream()
                .map(e -> this.collectionMapper.toDTO(e))
                .sorted(Comparator
                        .comparing((CollectionResponseDTO dto) -> Boolean.TRUE.equals(dto.favorite())).reversed()
                        .thenComparing(dto -> dto.deck().title().toLowerCase()))
                .toList();

        return new DefaultDTO("Coleções retornadas com sucesso", Boolean.TRUE, data, pagingDTO, null);
    }

    @Override
    @Transactional
    public DefaultDTO deleteCollection(String deckId) {
        User user = this.getAuthenticatedUser();
        Deck deck = this.findDeckById(UUIDConverter.toUUID(deckId));

        if(deck.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Você não pode remover sua propria coleção");
        }

        CollectionPk pk = this.generateCollectionPk(user, deck);
        Collection collection = this.findByCollectionPk(pk);

        collectionRepository.delete(collection);

        return new DefaultDTO("Coleção removida com sucesso", Boolean.TRUE, null, null, null);
    }

    private User getAuthenticatedUser() {
        return this.authProvider.getAuthenticatedUser();
    }

    private Deck findDeckById(UUID deckId) {
        return this.deckRepository.findById(deckId)
                .orElseThrow(() -> new EntityNotFoundException("A coleção não foi encontrada"));
    }

    private Collection findByCollectionPk(CollectionPk collectionPk) {
        return collectionRepository.findById(collectionPk)
                .orElseThrow(() -> new EntityNotFoundException("Essa coleção não foi encontrada"));
    }

    private CollectionPk generateCollectionPk(User user, Deck deck) {
        CollectionPk collectionPk = new CollectionPk();
        collectionPk.setUserId(user.getId());
        collectionPk.setDeckId(deck.getId());
        return collectionPk;
    }

    private void checkIsPublicAndCollectionBelongsUser(Deck deck) {
        if(!deck.getIsPublic() && !deck.getUser().getId().equals(this.getAuthenticatedUser().getId())) {
            throw new UnauthorizedException("Você não tem permissão sobre essa coleção");
        }
    }

    private String searchNormalize(String search) {
        if (search == null) return null;

        String normalizedSearch = Normalizer.normalize(search, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        return normalizedSearch.trim();
    }
}
