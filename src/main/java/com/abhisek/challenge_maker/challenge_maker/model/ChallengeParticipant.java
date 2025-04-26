package com.abhisek.challenge_maker.challenge_maker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "challenge_participants")
public class ChallengeParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID participantId;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false, referencedColumnName = "challenge_id", foreignKey = @ForeignKey(name = "fk_participant_challenge"))
    private Challenges challenges;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "fk_participant_user"))
    private User user;

    @Column(nullable = false, length = 50, columnDefinition = "VARCHAR(50) DEFAULT 'pending'")
    private String status; // 'pending', 'accepted', 'completed'

    @CreationTimestamp
    private LocalDateTime joinedAt;

    // Getters and Setters
}

