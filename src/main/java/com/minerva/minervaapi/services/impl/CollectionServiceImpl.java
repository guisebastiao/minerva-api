package com.minerva.minervaapi.services.impl;

import com.minerva.minervaapi.controllers.dtos.*;
import com.minerva.minervaapi.controllers.mappers.CollectionMapper;
import com.minerva.minervaapi.exceptions.BadRequestException;
import com.minerva.minervaapi.exceptions.EntityNotFoundException;
import com.minerva.minervaapi.exceptions.UnauthorizedException;
import com.minerva.minervaapi.models.*;
import com.minerva.minervaapi.models.Collection;
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

import java.util.*;
import java.util.stream.Collectors;

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
        for (Flashcard flashcard : deck.getFlashcards()) {
            Optional<Review> existingReview = reviewRepository.findByFlashcardAndDeckAndUser(flashcard, deck, user);

            if (existingReview.isEmpty()) {
                Review review = new Review();
                review.setDeck(deck);
                review.setUser(user);
                review.setFlashcard(flashcard);
                reviews.add(review);
            } else {
                System.out.println("Revisão já existe para flashcard_id=" + flashcard.getId() + ", deck_id=" + deck.getId() + ", user_id=" + user.getId());
            }
        }

        if (!reviews.isEmpty()) {
            try {
                this.reviewRepository.saveAll(reviews);
                System.out.println("Criadas " + reviews.size() + " revisões para usuário " + user.getUsername());
            } catch (RuntimeException e) {
                System.err.println("Erro ao salvar revisões: " + e.getMessage() + ", flashcard_ids=" + reviews.stream().map(r -> r.getFlashcard().getId().toString()).collect(Collectors.joining(",")));
                return new DefaultDTO("Erro ao adicionar coleção devido a revisões duplicadas", Boolean.FALSE, null, null, null);
            }
        } else {
            System.out.println("Nenhuma nova revisão criada para usuário " + user.getUsername());
        }

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
    public DefaultDTO findAllCollections(int offset, int limit) {
        User user = getAuthenticatedUser();

        Pageable pageable = PageRequest.of(offset, limit);

        Page<Collection> resultPage = collectionRepository.findAllByUser(user, pageable);

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
}
