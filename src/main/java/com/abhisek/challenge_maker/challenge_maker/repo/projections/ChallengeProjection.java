package com.abhisek.challenge_maker.challenge_maker.repo.projections;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ChallengeProjection {
    String getChallengeId();
    Integer getMadeById();
    String getTitle();
    String getDescription();
    String getInstruction();
    String getVideoLink();
    Integer getIsParticipated();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}
