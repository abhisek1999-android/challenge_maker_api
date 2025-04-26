package com.abhisek.challenge_maker.challenge_maker.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "challenges")
public class Challenges {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "challenge_id")
    private UUID challengeId;

    @ManyToOne
    @JoinColumn(name = "made_by_id", nullable = false, referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "fk_challenge_user"))
    private User madeBy;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String instruction;

    @Column(name = "video_link", columnDefinition = "TEXT")
    private String videoLink;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Getters and Setters
}

