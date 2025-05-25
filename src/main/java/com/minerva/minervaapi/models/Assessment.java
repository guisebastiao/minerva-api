package com.minerva.minervaapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "assessments")
public class Assessment {

    @EmbeddedId
    private AssessmentPk id = new AssessmentPk();

    @Column(name = "assessment_value", nullable = false)
    private Integer assessmentValue;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("deckId")
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;
}
