package com.minerva.minervaapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
public class CollectionPk implements Serializable {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "deck_id")
    private UUID deckId;
}
