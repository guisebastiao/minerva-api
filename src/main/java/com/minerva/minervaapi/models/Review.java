package com.minerva.minervaapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "easiness_factor", nullable = false)
    private Double easinessFactor = 2.5;

    @Column(name = "repetition", nullable = false)
    private Integer repetition = 0;

    @Column(name = "reviewed_at", nullable = false)
    private LocalDateTime reviewedAt = LocalDateTime.now();

    @Column(name = "next_review_date", nullable = false)
    private LocalDate nextReviewDate = LocalDate.now();

    @Column(name = "days_interval", nullable = false)
    private Integer daysInterval = 1;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "flashcard_id", nullable = false)
    private Flashcard flashcard;

    @ManyToOne
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;
}
