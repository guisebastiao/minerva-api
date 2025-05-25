package com.minerva.minervaapi.services.impl;

import com.minerva.minervaapi.controllers.dtos.*;
import com.minerva.minervaapi.controllers.mappers.CollectionMapper;
import com.minerva.minervaapi.exceptions.BadRequestException;
import com.minerva.minervaapi.exceptions.EntityNotFoundException;
import com.minerva.minervaapi.exceptions.UnauthorizedException;
import com.minerva.minervaapi.models.Collection;
import com.minerva.minervaapi.models.CollectionPk;
import com.minerva.minervaapi.models.Deck;
import com.minerva.minervaapi.models.User;
import com.minerva.minervaapi.repositories.CollectionRepository;
import com.minerva.minervaapi.repositories.DeckRepository;
import com.minerva.minervaapi.security.AuthProvider;
import com.minerva.minervaapi.services.CollectionService;
import com.minerva.minervaapi.utils.UUIDConverter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;

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

    @Override
    @Transactional
    public DefaultDTO addNewCollection(CollectionDTO collectionDTO) {
        Deck deck = this.findDeckById(UUIDConverter.toUUID(collectionDTO.deckId()));
        User user = this.getAuthenticatedUser();

        this.checkIsPublicAndCollectionBelongsUser(deck);

        Collection collection = new Collection();
        collection.setDeck(deck);
        collection.setUser(user);

        this.collectionRepository.save(collection);

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
