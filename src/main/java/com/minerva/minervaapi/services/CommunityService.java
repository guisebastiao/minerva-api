package com.minerva.minervaapi.services;

import com.minerva.minervaapi.controllers.dtos.DefaultDTO;

public interface CommunityService {
    DefaultDTO findAllPublicCollections(String search, String order, int offset, int limit);
}
