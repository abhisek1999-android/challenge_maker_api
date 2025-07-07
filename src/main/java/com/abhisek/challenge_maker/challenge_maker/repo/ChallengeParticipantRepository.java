package com.abhisek.challenge_maker.challenge_maker.repo;

import com.abhisek.challenge_maker.challenge_maker.dto.ChallengeDTO;
import com.abhisek.challenge_maker.challenge_maker.model.ChallengeParticipant;
import com.abhisek.challenge_maker.challenge_maker.repo.projections.ChallengeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, UUID> {

    List<ChallengeParticipant> findByChallenges_MadeBy_Id(int userId);
    List<ChallengeParticipant> findByChallenges_ChallengeId(UUID challengeId);


    @Modifying
    @Query(value = "UPDATE challenge_participant SET status =: status, challenge_completed_prof_video:=videoUrl " +
            "WHERE challenge_id:=challengeId AND made_by_id:=userId", nativeQuery = true)
    int updateChallengeParticipant(@Param("userId") int userId,
                                   @Param("challengeId") String challengeId,
                                   @Param("videoUrl") String videoUrl,
                                   @Param("status") String status
                                   );

}
