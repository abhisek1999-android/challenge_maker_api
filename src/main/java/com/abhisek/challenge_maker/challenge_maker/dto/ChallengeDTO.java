package com.abhisek.challenge_maker.challenge_maker.dto;

import com.abhisek.challenge_maker.challenge_maker.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeDTO {
    private UUID challengeId;
    private User madeBy;
    private String title;
    private String description;
    private String instruction;
    private String videoLink;
    private Boolean isParticipated;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
