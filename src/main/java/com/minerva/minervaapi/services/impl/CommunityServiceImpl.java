package com.minerva.minervaapi.services.impl;

import com.minerva.minervaapi.controllers.dtos.DeckResponseDTO;
import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.controllers.dtos.PagingDTO;
import com.minerva.minervaapi.controllers.mappers.DeckMapper;
import com.minerva.minervaapi.models.Deck;
import com.minerva.minervaapi.repositories.DeckRepository;
import com.minerva.minervaapi.services.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityServiceImpl implements CommunityService {

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private DeckMapper deckMapper;

    @Override
    public DefaultDTO findAllPublicCollections(String search, String order, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit, this.sorting(order));

        Page<Deck> resultPage = this.deckRepository.findAllByIsPublicAndTitleContainingIgnoreCase(true, search, pageable);

        PagingDTO pagingDTO = new PagingDTO(resultPage.getTotalElements(), resultPage.getTotalPages(), offset, limit);

        List<DeckResponseDTO> data = resultPage.getContent().stream()
                .map(this.deckMapper::toResponseDTO)
                .toList();

        return new DefaultDTO("Coleções retornadas com sucesso", Boolean.TRUE, data, pagingDTO, null);
    }

    private Sort sorting(String order) {
        Sort sort;

        switch (order.toLowerCase()) {
            case "assessment": {
                sort = Sort.by(Sort.Direction.DESC, "assessment");
                break;
            }
            case "date": {
                sort = Sort.by(Sort.Direction.DESC, "createdAt");
                break;
            }
            default: {
                sort = Sort.unsorted();
            }
        }

        return sort;
    }
}
