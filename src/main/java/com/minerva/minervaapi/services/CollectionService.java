package com.minerva.minervaapi.services;

import com.minerva.minervaapi.controllers.dtos.CollectionDTO;
import com.minerva.minervaapi.controllers.dtos.CollectionFavoriteDTO;
import com.minerva.minervaapi.controllers.dtos.DefaultDTO;

public interface CollectionService {
    DefaultDTO addNewCollection(CollectionDTO collectionDTO);
    DefaultDTO addFavorite(String deckId, CollectionFavoriteDTO collectionFavoriteDTO);
    DefaultDTO findAllCollectionsToStudy(String deckId, int offset, int limit);
    DefaultDTO findAllCollections(String search, int offset, int limit);
    DefaultDTO deleteCollection(String deckId);
}
