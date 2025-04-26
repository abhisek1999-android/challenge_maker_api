package com.abhisek.challenge_maker.challenge_maker.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "challenge_shares")
public class ChallengeShare {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID shareId;

    @ManyToOne
    @JoinColumn(name = "share_from", nullable = false, referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "fk_share_from_user"))
    private User shareFrom;

    @ManyToOne
    @JoinColumn(name = "share_to", nullable = false, referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "fk_share_to_user"))
    private User shareTo;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false, referencedColumnName = "challenge_id", foreignKey = @ForeignKey(name = "fk_share_challenge"))
    private Challenges challenges;

    @CreationTimestamp
    private LocalDateTime sharedAt;

    // Getters and Setters
}

