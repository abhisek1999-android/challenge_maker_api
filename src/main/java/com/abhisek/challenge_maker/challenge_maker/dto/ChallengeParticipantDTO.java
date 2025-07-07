package com.abhisek.challenge_maker.challenge_maker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeParticipantDTO {
    private UUID challengeId;
    private Long userId;  // Assuming User.id is Long type
    private String status;
    private String challengeCompletedProfVideo;
}