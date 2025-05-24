package com.minerva.minervaapi.controllers.mappers;

import com.minerva.minervaapi.models.Deck;
import com.minerva.minervaapi.security.AuthProvider;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeckMapperHelper {

    @Autowired
    private AuthProvider authProvider;

    @Named("isAuthUser")
    public Boolean isAuthUser(Deck deck) {
        return authProvider.getAuthenticatedUser().getId().equals(deck.getUser().getId());
    }
}