package com.minerva.minervaapi.services.impl;

import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.exceptions.EntityNotFoundException;
import com.minerva.minervaapi.exceptions.UnauthorizedException;
import com.minerva.minervaapi.models.Flashcard;
import com.minerva.minervaapi.models.User;
import com.minerva.minervaapi.repositories.FlashcardRepository;
import com.minerva.minervaapi.security.AuthProvider;
import com.minerva.minervaapi.services.FlashcardService;
import com.minerva.minervaapi.utils.UUIDConverter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlashcardServiceImpl implements FlashcardService {

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private AuthProvider authProvider;

    @Override
    @Transactional
    public DefaultDTO deleteFlashcard(String flashcardId) {
        Flashcard flashcard = this.findFlashcardById(flashcardId);

        this.checkFlashcardBelongToUser(flashcard.getDeck().getUser());

        this.flashcardRepository.delete(flashcard);

        return new DefaultDTO("Flashcard excluido com sucesso", true, null, null, null);
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

    private User getAuthenticatedUser() {
        return this.authProvider.getAuthenticatedUser();
    }
}
