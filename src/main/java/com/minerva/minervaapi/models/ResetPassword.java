package com.minerva.minervaapi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "reset_passwords")
public class ResetPassword {

    @Id
    @Column(name = "user_id")
    private UUID id;

    @Column(unique = true, nullable = false)
    private UUID token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
