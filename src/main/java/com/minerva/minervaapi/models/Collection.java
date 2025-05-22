package com.minerva.minervaapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "collections")
public class Collection {

    @EmbeddedId
    private CollectionPk id = new CollectionPk();

    @Column(nullable = false)
    private Boolean favorite = false;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("deckId")
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;
}
